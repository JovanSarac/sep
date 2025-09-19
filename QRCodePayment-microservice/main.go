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
	"strings"
	"syscall"
	"time"

	"github.com/MicahParks/keyfunc"
	"github.com/golang-jwt/jwt/v4"
	"github.com/google/uuid"
)

var keycloakJWKS *keyfunc.JWKS

type PaymentDataQR struct {
	PaymentId   int64     `json:"paymentId"`
	PaymentUrl  string    `json:"paymentUrl"`
	QRData      string    `json:"qrData"`
	QrPaymentId uuid.UUID `json:"qrPaymentId"`
}

type RequestPaymentQRDto struct {
	PaymentId   int64     `json:"paymentId"`
	PaymentUrl  string    `json:"paymentUrl"`
	Amount      float64   `json:"amount"`
	SuccessUrl  string    `json:"successUrl"`
	FailedUrl   string    `json:"failedUrl"`
	ErrorUrl    string    `json:"errorUrl"`
	QRData      string    `json:"qrData"`
	QrPaymentId uuid.UUID `json:"qrPaymentId"`
}

type RequestDto struct {
	MerchantId       uuid.UUID `json:"merchantId"`
	MerchantPassword string    `json:"merchantPassword"`
	Amount           float64   `json:"amount"`
	MerchantOrderId  int64     `json:"merchantOrderId"`
	Timestamp        int64     `json:"timestamp"`
	SuccessUrl       string    `json:"successUrl"`
	FailedUrl        string    `json:"failedUrl"`
	ErrorUrl         string    `json:"errorUrl"`
}

func registerWithConsul(serviceName string, port int) {
	consulURL := "http://localhost:8500/v1/agent/service/register"

	data := map[string]interface{}{
		"Name":    serviceName,
		"Address": "host.docker.internal",
		"Port":    port,
		"Check": map[string]interface{}{
			"HTTP":     fmt.Sprintf("http://host.docker.internal:%d/health", port),
			"Interval": "10s",
		},
	}

	body, _ := json.Marshal(data)
	req, err := http.NewRequest(http.MethodPut, consulURL, bytes.NewReader(body))
	if err != nil {
		log.Fatalf("Failed to create request: %v", err)
	}
	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		log.Fatalf("Failed to register service: %v", err)
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		bodyBytes, _ := io.ReadAll(resp.Body)
		log.Fatalf("Failed to register service: %s", string(bodyBytes))
	}

	log.Printf("Registered %s with Consul", serviceName)

	//send notification to psp
	url := "https://localhost:9000/newPaymentService/create"
	serviceNameBody := serviceName

	serviceReq, serviceErr := http.NewRequest("POST", url, bytes.NewBufferString(serviceNameBody))
	if serviceErr != nil {
		panic(serviceErr)
	}

	// Set headers (optional, but usually good)
	serviceReq.Header.Set("Content-Type", "text/plain")

	httpClient := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true},
		},
	}

	// Send request
	serviceResp, err := httpClient.Do(serviceReq)
	if err != nil {
		panic(err)
	}
	defer serviceResp.Body.Close()
}

func initKeycloak() {
	jwksURL := "http://localhost:8080/realms/sep-realm/protocol/openid-connect/certs" // zameni sa tvojim Keycloak URL-om

	// Kreiramo JWKS sa automatskim osvežavanjem svakih 10 minuta
	options := keyfunc.Options{
		RefreshInterval: time.Minute * 10,
		RefreshErrorHandler: func(err error) {
			fmt.Printf("Greška prilikom osvežavanja JWKS: %v\n", err)
		},
	}

	var err error
	keycloakJWKS, err = keyfunc.Get(jwksURL, options)
	if err != nil {
		panic(fmt.Sprintf("Ne mogu da učitam JWKS: %v", err))
	}

	fmt.Println("Keycloak JWKS učitan i inicijalizovan")
}

func validateToken(r *http.Request) (*jwt.Token, error) {
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		return nil, fmt.Errorf("missing Authorization header")
	}

	parts := strings.Split(authHeader, " ")
	if len(parts) != 2 || parts[0] != "Bearer" {
		return nil, fmt.Errorf("invalid Authorization header format")
	}

	tokenString := parts[1]

	token, err := jwt.Parse(tokenString, keycloakJWKS.Keyfunc)
	if err != nil {
		return nil, err
	}

	if !token.Valid {
		return nil, fmt.Errorf("invalid token")
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok {
		return nil, fmt.Errorf("invalid claims")
	}

	audClaim, ok := claims["aud"]
	if !ok {
		return nil, fmt.Errorf("missing aud claim")
	}

	validAud := false

	switch v := audClaim.(type) {
	case string:
		if v == "sep-qr-payment-microservice" {
			validAud = true
		}
	case []interface{}:
		for _, a := range v {
			if aStr, ok := a.(string); ok && aStr == "sep-qr-payment-microservice" {
				validAud = true
				break
			}
		}
	default:
		return nil, fmt.Errorf("invalid aud claim type")
	}

	if !validAud {
		return nil, fmt.Errorf("token not intended for this service")
	}

	return token, nil
}

func main() {
	// Make a channel to receive signals
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)
	initKeycloak()

	// Set up routes
	http.HandleFunc("/bank1QRCodeValidateRequest", validateRequest)
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	fmt.Println("QRCodePayment microservice is running on :8083")

	// Create server object
	srv := &http.Server{Addr: ":8083"}

	// Start HTTP server in a goroutine
	go func() {
		registerWithConsul("qrCode", 8083)

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("ListenAndServe error: %v", err)
		}
	}()

	// Wait until a shutdown signal is received
	<-stop
	fmt.Println("Shutting down...")

	// Gracefully stop the HTTP server
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	if err := srv.Shutdown(ctx); err != nil {
		log.Printf("HTTP server shutdown error: %v", err)
	}

	deregisterFromConsul("qrCode")
	// Notify PSP after shutdown
	notifyPSP("qrCode")
}

func validateRequest(w http.ResponseWriter, r *http.Request) {
	_, err := validateToken(r)
	if err != nil {
		http.Error(w, "Unauthorized: "+err.Error(), http.StatusUnauthorized)
		return
	}

	body, err := io.ReadAll(r.Body)
	if err != nil {
		http.Error(w, "Failed to read request body", http.StatusBadRequest)
		return
	}

	fmt.Println("Received body:", string(body))

	var requestDto RequestDto
	err = json.Unmarshal(body, &requestDto)
	if err != nil {
		http.Error(w, "Failed to parse request DTO", http.StatusBadRequest)
		return
	}

	fmt.Println("Request DTO: ", requestDto)

	token := r.Header.Get("Authorization")
	req, err := http.NewRequest("POST",
		"https://localhost:8091/api/bank1/requests/validateRequestQRCode",
		bytes.NewBuffer(body),
	)

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", token)

	if err != nil {
		http.Error(w, "Failed to create request", http.StatusInternalServerError)
		return
	}

	client := &http.Client{}
	//resp, err := http.Post(fmt.Sprintf("https://localhost:8091/api/bank1/requests/validateRequestQRCode"), "application/json", bytes.NewBuffer(body))
	resp, err := client.Do(req)
	fmt.Println("BILO STA")
	if err != nil {
		fmt.Println("Error making HTTP request:", err)
		http.Error(w, "Failed to validate request", http.StatusInternalServerError)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		fmt.Println("Unexpected status code:", resp.StatusCode)
		http.Error(w, "Validation failed", http.StatusInternalServerError)
		return
	}

	var paymentDataQR PaymentDataQR

	err = json.NewDecoder(resp.Body).Decode(&paymentDataQR)
	if err != nil {
		http.Error(w, "Failed to parse response", http.StatusInternalServerError)
		return
	}

	var requestPaymentQRDto RequestPaymentQRDto

	requestPaymentQRDto.Amount = requestDto.Amount
	requestPaymentQRDto.ErrorUrl = requestDto.ErrorUrl
	requestPaymentQRDto.FailedUrl = requestDto.FailedUrl
	requestPaymentQRDto.SuccessUrl = requestDto.SuccessUrl
	requestPaymentQRDto.PaymentId = paymentDataQR.PaymentId
	requestPaymentQRDto.PaymentUrl = paymentDataQR.PaymentUrl
	requestPaymentQRDto.QRData = paymentDataQR.QRData
	requestPaymentQRDto.QrPaymentId = paymentDataQR.QrPaymentId

	fmt.Println("Response data:", requestPaymentQRDto)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(requestPaymentQRDto)
}

func notifyPSP(serviceName string) {
	url := "https://localhost:9000/newPaymentService/remove"

	req, err := http.NewRequest("POST", url, bytes.NewBufferString(serviceName))
	if err != nil {
		fmt.Println("Request build error:", err)
		return
	}
	req.Header.Set("Content-Type", "text/plain")

	client := &http.Client{
		Transport: &http.Transport{
			TLSClientConfig: &tls.Config{InsecureSkipVerify: true}, // only for dev
		},
		Timeout: 5 * time.Second, // don’t hang forever
	}

	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()
	req = req.WithContext(ctx)

	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("Request error:", err)
		return
	}
	defer resp.Body.Close()
}

func deregisterFromConsul(serviceName string) {
	consulURL := fmt.Sprintf("http://localhost:8500/v1/agent/service/deregister/%s", serviceName)

	req, err := http.NewRequest(http.MethodPut, consulURL, nil)
	if err != nil {
		log.Printf("[%s] Failed to create deregistration request: %v", serviceName, err)
		return
	}

	client := &http.Client{Timeout: 5 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		log.Printf("[%s] Failed to deregister from Consul: %v", serviceName, err)
		return
	}
	defer resp.Body.Close()

	log.Printf("[%s] Deregistered from Consul", serviceName)

	// Wait a moment for Consul to process the deregistration
	time.Sleep(1 * time.Second)
}
