package main

import (
	"bytes"
	"context"
	"crypto/tls"
	"encoding/base64"
	"encoding/json"
	"ethereum-payment-microservice/models"
	"fmt"
	"html"
	"io"
	"log"
	"net/http"
	"os"
	"os/signal"
	"regexp"
	"strings"
	"syscall"
	"time"

	"github.com/prometheus/client_golang/prometheus/promhttp"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"

	"go.opentelemetry.io/otel"
	"go.opentelemetry.io/otel/exporters/otlp/otlptrace/otlptracehttp"
	"go.opentelemetry.io/otel/sdk/resource"
	sdktrace "go.opentelemetry.io/otel/sdk/trace"
	semconv "go.opentelemetry.io/otel/semconv/v1.12.0"
)

var database *gorm.DB

func initDB() *gorm.DB {
	connStr := "user=postgres dbname=EthPayment password=super sslmode=disable"
	db, err := gorm.Open(postgres.Open(connStr), &gorm.Config{})
	if err != nil {
		panic(err)
	}
	return db
}

func initTracer() func() {
	ctx := context.Background()

	// Create OTLP HTTP exporter
	exp, err := otlptracehttp.New(ctx,
		otlptracehttp.WithEndpoint("localhost:4318"),
		otlptracehttp.WithInsecure(), // Required unless using HTTPS
	)
	if err != nil {
		log.Fatalf("failed to create OTLP trace exporter: %v", err)
	}

	tp := sdktrace.NewTracerProvider(
		sdktrace.WithBatcher(exp),
		sdktrace.WithResource(resource.NewWithAttributes(
			semconv.SchemaURL,
			semconv.ServiceNameKey.String("ETHPAYMENT"),
		)),
	)

	otel.SetTracerProvider(tp)

	return func() {
		if err := tp.Shutdown(ctx); err != nil {
			log.Fatalf("Error shutting down tracer provider: %v", err)
		}
	}
}

var tracer = otel.Tracer("eth-payment-service")

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

func main() {
	logFilePath := "F:/Nevena/faks/master/SEP/projekat/sep/monitoring/logs/ethPayment.log"
	logFile, err := os.OpenFile(logFilePath, os.O_CREATE|os.O_WRONLY|os.O_APPEND, 0666)
	if err != nil {
		log.Fatalf("[EthPayment] Failed to open log file: %v", err)
	}
	log.SetOutput(logFile)

	database = initDB()
	if database == nil {
		log.Println("FAILED TO CONNECT TO DB")
		return
	}

	shutdown := initTracer()
	defer shutdown()

	// Make a channel to receive signals
	stop := make(chan os.Signal, 1)
	signal.Notify(stop, syscall.SIGINT, syscall.SIGTERM)

	// Register handlers
	http.HandleFunc("/eth", getWalletIds)
	http.HandleFunc("/eth/saveTransaction", saveTransaction)
	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})
	http.Handle("/metrics", promhttp.Handler())

	// Create server object
	srv := &http.Server{Addr: ":8084"}

	// Start HTTP server in a goroutine (so main thread can catch signals)
	go func() {
		log.Println("[EthPayment] EthService is running on :8084")

		if err := srv.ListenAndServe(); err != nil && err != http.ErrServerClosed {
			log.Fatalf("ListenAndServe error: %v", err)
		}
	}()

	// Wait for server to start, then register
	time.Sleep(2 * time.Second)
	registerWithConsul("eth", 8084)

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
	notifyPSP("eth")

	fmt.Println("Shutdown complete.")
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

func getWalletIds(w http.ResponseWriter, r *http.Request) {
	log.Println("[EthPayment] /eth called")
	w.Header().Set("Content-Type", "application/json")

	ctx, span := tracer.Start(r.Context(), "getWalletIds")
	defer span.End()

	var users []models.User
	if err := database.WithContext(ctx).Find(&users).Error; err != nil {
		log.Printf("[EthPayment] Failed to fetch users: %v", err)
		http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
		return
	}

	var walletIds []string
	for _, user := range users {
		decodedWalletIdBytes, err := base64.StdEncoding.DecodeString(user.WalletId)
		if err != nil {
			log.Printf("[EthPayment] Failed to decode WalletId for user Id %d: %v", user.Id, err)
			http.Error(w, "Failed to decode wallet ID", http.StatusInternalServerError)
			return
		}
		walletIds = append(walletIds, string(decodedWalletIdBytes))
	}

	log.Printf("[EthPayment] Returning %d wallet ids", len(walletIds))
	json.NewEncoder(w).Encode(walletIds)
}

func saveTransaction(w http.ResponseWriter, r *http.Request) {
	log.Println("[EthPayment] /eth/saveTransaction called")
	w.Header().Set("Content-Type", "application/json")

	ctx, span := tracer.Start(r.Context(), "saveTransaction")
	defer span.End()

	var tx models.Transaction
	if err := json.NewDecoder(r.Body).Decode(&tx); err != nil {
		log.Printf("[EthPayment] Failed to decode request body: %v", err)
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	log.Printf("[EthPayment] Received transaction: %+v", tx)

	//remove scripts and spaces
	originalSender := tx.SenderWalletId
	tx.SenderWalletId = sanitize(tx.SenderWalletId)
	if originalSender != tx.SenderWalletId {
		log.Printf("[EthPayment] senderWalletId contained disallowed characters")
		http.Error(w, "senderWalletId contains invalid characters", http.StatusBadRequest)
		return
	}

	originalReceiver := tx.ReceiverWalletId
	tx.ReceiverWalletId = sanitize(tx.ReceiverWalletId)
	if originalReceiver != tx.ReceiverWalletId {
		log.Printf("[EthPayment] receiverWalletId contained disallowed characters")
		http.Error(w, "receiverWalletId contains invalid characters", http.StatusBadRequest)
		return
	}

	originalHash := tx.TransactionHash
	tx.TransactionHash = sanitize(tx.TransactionHash)
	if originalHash != tx.TransactionHash {
		log.Printf("[EthPayment] transactionHash contained disallowed characters")
		http.Error(w, "transactionHash contains invalid characters", http.StatusBadRequest)
		return
	}

	//validate amount
	if tx.Amount <= 0 {
		log.Printf("[EthPayment] Amount must be positive and non-zero")
		http.Error(w, "Amount must be positive and non-zero", http.StatusBadRequest)
		return
	}
	if tx.Amount > 1_000_000_000_000 {
		log.Printf("[EthPayment] Amount too large")
		http.Error(w, "Amount too large", http.StatusBadRequest)
		return
	}

	//validate wallet ids and transaction hash formats
	if !isValidWalletId(tx.SenderWalletId) {
		log.Printf("[EthPayment] Invalid sender wallet ID format")
		http.Error(w, "Invalid sender wallet ID format", http.StatusBadRequest)
		return
	}
	if !isValidWalletId(tx.ReceiverWalletId) {
		log.Printf("[EthPayment] Invalid receiver wallet ID format")
		http.Error(w, "Invalid receiver wallet ID format", http.StatusBadRequest)
		return
	}
	if !isValidTxHash(tx.TransactionHash) {
		log.Printf("[EthPayment] Invalid transaction hash format")
		http.Error(w, "Invalid transaction hash format", http.StatusBadRequest)
		return
	}

	//check senderWalletId not empty after sanitization
	if len(tx.SenderWalletId) == 0 {
		log.Printf("[EthPayment] senderWalletId is empty after sanitization")
		http.Error(w, "senderWalletId cannot be empty", http.StatusBadRequest)
		return
	}

	//check receiverWalletId not empty after sanitization
	if len(tx.ReceiverWalletId) == 0 {
		log.Printf("[EthPayment] receiverWalletId is empty after sanitization")
		http.Error(w, "receiverWalletId cannot be empty", http.StatusBadRequest)
		return
	}

	//check transaction hash uniqueness
	var existingTx models.Transaction
	err := database.WithContext(ctx).
		Where(`"transactionHash" = ?`, tx.TransactionHash).
		First(&existingTx).Error
	if err == nil {
		log.Printf("[EthPayment] Duplicate transaction hash detected: %s", tx.TransactionHash)
		http.Error(w, "Transaction hash already exists", http.StatusConflict)
		return
	} else if err != gorm.ErrRecordNotFound {
		log.Printf("[EthPayment] DB error checking transaction hash: %v", err)
		http.Error(w, "Internal server error", http.StatusInternalServerError)
		return
	}

	//defensive base64 encode senderWalletId
	encodedSender := base64.StdEncoding.EncodeToString([]byte(tx.SenderWalletId))
	if encodedSender == "" {
		log.Printf("[EthPayment] Failed to encode senderWalletId")
		http.Error(w, "Invalid senderWalletId", http.StatusBadRequest)
		return
	}
	tx.SenderWalletId = encodedSender

	//defensive base64 encode receiverWalletId
	encodedReceiver := base64.StdEncoding.EncodeToString([]byte(tx.ReceiverWalletId))
	if encodedReceiver == "" {
		log.Printf("[EthPayment] Failed to encode receiverWalletId")
		http.Error(w, "Invalid receiverWalletId", http.StatusBadRequest)
		return
	}
	tx.ReceiverWalletId = encodedReceiver

	if err := database.WithContext(ctx).Create(&tx).Error; err != nil {
		log.Printf("[EthPayment] Failed to save transaction: %v", err)
		http.Error(w, "Failed to save transaction", http.StatusInternalServerError)
		return
	}

	log.Printf("[EthPayment] Transaction saved: ID=%d", tx.Id)
	w.WriteHeader(http.StatusCreated)
}

func sanitize(input string) string {
	//remove script tags
	scriptTag := regexp.MustCompile(`(?i)<script.*?>.*?</script>`)
	cleaned := scriptTag.ReplaceAllString(input, "")

	//escape all HTML entities (e.g. <, >, &, ', ") to prevent XSS
	escaped := html.EscapeString(cleaned)

	//trim whitespace and restrict input length
	return strings.TrimSpace(escaped)
}

func isValidTxHash(hash string) bool {
	match, _ := regexp.MatchString(`^0x[a-fA-F0-9]{64}$`, hash)
	return match
}

func isValidWalletId(walletId string) bool {
	match, _ := regexp.MatchString(`^0x[a-fA-F0-9]{40}$`, walletId)
	return match
}
