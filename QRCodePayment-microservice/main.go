package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"
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
	initKeycloak()
	fmt.Println("QRCodePayment microservice is running on :8083")
	http.HandleFunc("/bank1QRCodeValidateRequest", validateRequest)

	http.HandleFunc("/health", func(w http.ResponseWriter, r *http.Request) {
		w.WriteHeader(http.StatusOK)
		w.Write([]byte("OK"))
	})

	registerWithConsul("qrCode", 8083)

	http.ListenAndServe(":8083", nil)
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
		return
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		fmt.Println("Unexpected status code:", resp.StatusCode)
		fmt.Println(resp)
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

	fmt.Println("PODACI")
	fmt.Print(requestPaymentQRDto)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(requestPaymentQRDto)
}
