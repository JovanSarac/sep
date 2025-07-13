package main

import (
	"context"
	"encoding/base64"
	"encoding/json"
	"ethereum-payment-microservice/models"
	"log"
	"net/http"
	"os"

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

	http.HandleFunc("/eth", getWalletIds)
	http.HandleFunc("/eth/saveTransaction", saveTransaction)

	http.Handle("/metrics", promhttp.Handler())

	log.Println("[EthPayment] EthService is running on :8084")
	http.ListenAndServe(":8084", nil)
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

	tx.SenderWalletId = base64.StdEncoding.EncodeToString([]byte(tx.SenderWalletId))
	tx.ReceiverWalletId = base64.StdEncoding.EncodeToString([]byte(tx.ReceiverWalletId))

	if err := database.WithContext(ctx).Create(&tx).Error; err != nil {
		log.Printf("[EthPayment] Failed to save transaction: %v", err)
		http.Error(w, "Failed to save transaction", http.StatusInternalServerError)
		return
	}

	log.Printf("[EthPayment] Transaction saved: ID=%d", tx.Id)
	w.WriteHeader(http.StatusCreated)
}
