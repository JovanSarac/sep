package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"

	"github.com/google/uuid"
)

type PaymentDataQR struct {
	PaymentId  int64  `json:"paymentId"`
	PaymentUrl string `json:"paymentUrl"`
	QRData     string `json:"qrData"`
}

type RequestPaymentQRDto struct {
	PaymentId  int64   `json:"paymentId"`
	PaymentUrl string  `json:"paymentUrl"`
	Amount     float64 `json:"amount"`
	SuccessUrl string  `json:"successUrl"`
	FailedUrl  string  `json:"failedUrl"`
	ErrorUrl   string  `json:"errorUrl"`
	QRData     string  `json:"qrData"`
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

func main() {
	fmt.Println("QRCodePayment microservice is running on :8083")
	http.HandleFunc("/bank1QRCodeValidateRequest", validateRequest)
	http.ListenAndServe(":8083", nil)
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

	resp, err := http.Post(fmt.Sprintf("http://localhost:8091/api/bank1/requests/validateRequestQRCode"), "application/json", bytes.NewBuffer(body))
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

	fmt.Print(requestPaymentQRDto)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(requestPaymentQRDto)
}
