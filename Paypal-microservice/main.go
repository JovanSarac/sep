package main

import (
	"bytes"
	"context"
	"crypto/tls"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"syscall"
	"time"

	"github.com/joho/godotenv"
	paypal "github.com/plutov/paypal/v4"
)

var (
	paypalClient *paypal.Client
	serviceName  = "paypal"
	port         = 8086
	consulAddr   = "http://localhost:8500"
	pspNotify    = "https://localhost:9000/newPaymentService/create"
	pspRemove    = "https://localhost:9000/newPaymentService/remove"
)

type PaypalPaymentDto struct {
	PaymentId   string `json:"paymentId"`
	ApprovalUrl string `json:"approvalUrl"`
}

// initPaypal inicijalizuje paypal SDK i uzme token
func initPaypal() {
	clientID := os.Getenv("PAYPAL_CLIENT_ID")
	secret := os.Getenv("PAYPAL_SECRET")
	env := os.Getenv("PAYPAL_ENV") // sandbox|live

	if clientID == "" || secret == "" {
		log.Fatal("PAYPAL_CLIENT_ID and PAYPAL_SECRET must be set")
	}

	var c *paypal.Client
	var err error
	if env == "live" {
		c, err = paypal.NewClient(clientID, secret, paypal.APIBaseLive)
	} else {
		c, err = paypal.NewClient(clientID, secret, paypal.APIBaseSandBox)
	}
	if err != nil {
		log.Fatalf("failed to create paypal client: %v", err)
	}

	// get access token
	_, err = c.GetAccessToken(context.Background())
	if err != nil {
		log.Fatalf("failed to get paypal access token: %v", err)
	}
	paypalClient = c
	log.Println("[paypal] initialized")
}

func registerWithConsul(name string, port int) {
	consulEnv := os.Getenv("CONSUL_ADDR")
	if consulEnv != "" {
		consulAddr = consulEnv
	}
	consulURL := fmt.Sprintf("%s/v1/agent/service/register", consulAddr)

	data := map[string]interface{}{
		"Name":    name,
		"Address": "localhost",
		"Port":    port,
		"Check": map[string]interface{}{
			"HTTP":     fmt.Sprintf("http://localhost:%d/health", port),
			"Interval": "10s",
		},
	}

	body, _ := json.Marshal(data)
	req, err := http.NewRequest(http.MethodPut, consulURL, bytes.NewReader(body))
	if err != nil {
		log.Fatalf("Failed to create consul request: %v", err)
	}
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{Timeout: 5 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		log.Fatalf("Failed to register service in consul: %v", err)
	}
	defer resp.Body.Close()
	if resp.StatusCode/100 != 2 {
		b, _ := io.ReadAll(resp.Body)
		log.Fatalf("Consul register failed: %s", string(b))
	}
	log.Printf("Registered %s with Consul", name)

	// notify PSP create
	psp := os.Getenv("PSP_NOTIFY_URL")
	if psp != "" {
		pspNotify = psp
	}
	req2, _ := http.NewRequest("POST", pspNotify, bytes.NewBufferString(name))
	req2.Header.Set("Content-Type", "text/plain")

	httpClient := &http.Client{
		Transport: &http.Transport{TLSClientConfig: &tls.Config{InsecureSkipVerify: true}},
		Timeout:   5 * time.Second,
	}
	resp2, err := httpClient.Do(req2)
	if err != nil {
		log.Printf("PSP notify (create) error: %v", err)
	} else {
		resp2.Body.Close()
		log.Printf("PSP notified about create: %d", resp2.StatusCode)
	}
}

// deregisterFromConsul deregistruje servis i notify-uje PSP remove endpoint
func deregisterFromConsul(name string) {
	consulEnv := os.Getenv("CONSUL_ADDR")
	if consulEnv != "" {
		consulAddr = consulEnv
	}
	url := fmt.Sprintf("%s/v1/agent/service/deregister/%s", consulAddr, name)
	req, _ := http.NewRequest("PUT", url, nil)
	client := &http.Client{Timeout: 5 * time.Second, Transport: &http.Transport{TLSClientConfig: &tls.Config{InsecureSkipVerify: true}}}
	resp, err := client.Do(req)
	if err != nil {
		log.Printf("Consul deregister error: %v", err)
	} else {
		resp.Body.Close()
		log.Printf("Deregistered %s from Consul (status %d)", name, resp.StatusCode)
	}

	// notify PSP remove
	pspR := os.Getenv("PSP_REMOVE_URL")
	if pspR != "" {
		pspRemove = pspR
	}
	req2, _ := http.NewRequest("POST", pspRemove, bytes.NewBufferString(name))
	req2.Header.Set("Content-Type", "text/plain")
	client2 := &http.Client{Timeout: 5 * time.Second, Transport: &http.Transport{TLSClientConfig: &tls.Config{InsecureSkipVerify: true}}}
	resp2, err := client2.Do(req2)
	if err != nil {
		log.Printf("PSP notify (remove) error: %v", err)
	} else {
		resp2.Body.Close()
		log.Printf("PSP notified about remove: %d", resp2.StatusCode)
	}
}

// health handler
func healthHandler(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}

func createOrderHandler(w http.ResponseWriter, r *http.Request) {
	type Req struct {
		Amount     string `json:"amount"`
		Currency   string `json:"currency"`
		SuccessURL string `json:"successUrl"`
		CancelURL  string `json:"cancelUrl"`
	}
	var req Req
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request", http.StatusBadRequest)
		return
	}

	purchaseUnits := []paypal.PurchaseUnitRequest{
		{
			Amount: &paypal.PurchaseUnitAmount{
				Currency: req.Currency,
				Value:    req.Amount,
			},
		},
	}

	appCtx := &paypal.ApplicationContext{
		ReturnURL: req.SuccessURL,
		CancelURL: req.CancelURL,
	}

	order, err := paypalClient.CreateOrder(context.Background(), "CAPTURE", purchaseUnits, nil, appCtx)
	if err != nil {
		http.Error(w, fmt.Sprintf("paypal error: %v", err), http.StatusInternalServerError)
		return
	}

	var approvalURL string
	for _, link := range order.Links {
		if link.Rel == "approve" {
			approvalURL = link.Href
			break
		}
	}

	json.NewEncoder(w).Encode(PaypalPaymentDto{
		PaymentId:   order.ID,
		ApprovalUrl: approvalURL,
	})
}

func captureOrderHandler(w http.ResponseWriter, r *http.Request) {
	type PaypalCaptureRequest struct {
		OrderID string `json:"orderId"`
		PayerID string `json:"payerId"`
	}

	var req PaypalCaptureRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid request body", http.StatusBadRequest)
		return
	}
	if req.OrderID == "" {
		http.Error(w, "orderId required", http.StatusBadRequest)
		return
	}

	capture, err := paypalClient.CaptureOrder(context.Background(), req.OrderID, paypal.CaptureOrderRequest{})
	if err != nil {
		http.Error(w, fmt.Sprintf("capture error: %v", err), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(capture)
}

func main() {
	err := godotenv.Load()
	if err != nil {
		log.Println("No .env file found, using system env")
	}
	if v := os.Getenv("SERVICE_NAME"); v != "" {
		serviceName = v
	}
	if v := os.Getenv("SERVICE_PORT"); v != "" {
		if p, err := strconv.Atoi(v); err == nil {
			port = p
		}
	}
	if v := os.Getenv("CONSUL_ADDR"); v != "" {
		consulAddr = v
	}
	if v := os.Getenv("PSP_NOTIFY_URL"); v != "" {
		pspNotify = v
	}
	if v := os.Getenv("PSP_REMOVE_URL"); v != "" {
		pspRemove = v
	}

	initPaypal()

	mux := http.NewServeMux()
	mux.HandleFunc("/health", healthHandler)
	mux.HandleFunc("/paypal/create-order", createOrderHandler)
	mux.HandleFunc("/paypal/capture-order", captureOrderHandler)

	srv := &http.Server{
		Addr:    fmt.Sprintf(":%d", port),
		Handler: mux,
	}

	// register & notify PSP
	go registerWithConsul(serviceName, port)

	// graceful shutdown
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)

	go func() {
		log.Printf("paypal service running on :%d", port)
		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("ListenAndServe error: %v", err)
		}
	}()

	<-stop
	log.Println("shutting down...")

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		log.Printf("server shutdown error: %v", err)
	}

	// deregister & notify PSP remove
	deregisterFromConsul(serviceName)
	log.Println("shutdown complete")
}
