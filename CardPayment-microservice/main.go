package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os/exec"
	"runtime"

	"github.com/google/uuid"
)

type PaymentData struct {
	PaymentId  int64  `json:"paymentId"`
	PaymentUrl string `json:"paymentUrl"`
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
	http.HandleFunc("/card", getCard)
	fmt.Println("CardService is running on :8082")
	http.HandleFunc("/bank1", goToBank1)
	http.ListenAndServe(":8082", nil)

	fs := http.FileServer(http.Dir("../Bank1/frontend/Bank1/dist/bank1"))
	http.Handle("/", fs)
}

func goToBank1(w http.ResponseWriter, r *http.Request) {
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

	resp, err := http.Post(fmt.Sprintf("http://localhost:8091/api/bank1/requests/validateRequest"), "application/json", bytes.NewBuffer(body))

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

	fmt.Println("Payment URL:", paymentData.PaymentUrl)

	fullUrl := fmt.Sprintf("%s?amount=%.2f", paymentData.PaymentUrl, requestDto.Amount)
	fmt.Println("%s?data=%s&amount=%.2f", paymentData.PaymentUrl, requestDto.Amount)

	go func() {
		err := openBrowser(fullUrl)
		if err != nil {
			fmt.Println("Greška pri otvaranju pretraživača:", err)
		}
	}()

	port := ":4202"
	println("Serving frontend at http://localhost" + port)
	if err := http.ListenAndServe(port, nil); err != nil {
		fmt.Println(err)
		//panic(err)
	}
	fmt.Println("Ispis")

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
