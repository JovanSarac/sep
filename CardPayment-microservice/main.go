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
	"os/exec"
	"os/signal"
	"runtime"
	"syscall"
	"time"

	"github.com/google/uuid"
)

type PaymentData struct {
	PaymentId  int64  `json:"paymentId"`
	PaymentUrl string `json:"paymentUrl"`
}

type RequestPaymentDto struct {
	PaymentId  int64   `json:"paymentId"`
	PaymentUrl string  `json:"paymentUrl"`
	Amount     float64 `json:"amount"`
	SuccessUrl string  `json:"successUrl"`
	FailedUrl  string  `json:"failedUrl"`
	ErrorUrl   string  `json:"errorUrl"`
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

func main() {
	// Make a channel to receive signals
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)

	// Set up all routes before starting server
	http.HandleFunc("/card", getCard)
	http.HandleFunc("/bank1ValidateRequest", validateRequest)
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	// Serve static files
	fs := http.FileServer(http.Dir("../Bank1/frontend/Bank1/dist/bank1"))
	http.Handle("/", fs)

	fmt.Println("CardService is running on :8082")

	// Create server object with consistent port
	srv := &http.Server{Addr: ":8082"}

	// Start HTTP server in a goroutine
	go func() {
		// Register with Consul using the same port as the server
		registerWithConsul("card", 8082)

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

	// Notify PSP after shutdown
	notifyPSP("card")
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

func validateRequest(w http.ResponseWriter, r *http.Request) {
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

	resp, err := http.Post(fmt.Sprintf("https://localhost:8091/api/bank1/requests/validateRequest"), "application/json", bytes.NewBuffer(body))
	fmt.Println("BILO STA")
	if err != nil {
		fmt.Println("Error making HTTP request:", err)
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		fmt.Println("Unexpected status code:", resp.StatusCode)
		fmt.Println(resp)
		return
	}

	var paymentData PaymentData

	err = json.NewDecoder(resp.Body).Decode(&paymentData)
	if err != nil {
		http.Error(w, "Failed to parse response", http.StatusInternalServerError)
		return
	}

	var requestPaymentDto RequestPaymentDto

	requestPaymentDto.Amount = requestDto.Amount
	requestPaymentDto.ErrorUrl = requestDto.ErrorUrl
	requestPaymentDto.FailedUrl = requestDto.FailedUrl
	requestPaymentDto.SuccessUrl = requestDto.SuccessUrl
	requestPaymentDto.PaymentId = paymentData.PaymentId
	requestPaymentDto.PaymentUrl = paymentData.PaymentUrl

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(requestPaymentDto)
}

func openBrowser(url string) error {
	var cmd string
	var args []string

	switch runtime.GOOS {
	case "windows":
		cmd = "cmd"
		args = []string{"/c", "start", url}
	case "darwin":
		cmd = "open"
		args = []string{url}
	case "linux":
		cmd = "xdg-open"
		args = []string{url}
	default:
		return fmt.Errorf("Nepodržan operativni sistem")
	}

	return exec.Command(cmd, args...).Start()
}

func getCard(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintln(w, "Card data")
}
