package main

import (
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
)

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

	// register the handler function with the server

	router := mux.NewRouter()
	router.HandleFunc("/card", proxy("/card", "http://localhost:8082")).Methods("GET")
	router.HandleFunc("/bank1", proxy("/bank1", "http://localhost:8082")).Methods("POST")
	router.HandleFunc("/answerPSP", proxy("/answerPSP", "http://localhost:8090/api/psp/requests/answerPSP")).Methods("POST")

	log.Fatal(http.ListenAndServe(":8080", router))
}

func proxy(path, target string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		var requestDto RequestDto
		if err := json.NewDecoder(r.Body).Decode(&requestDto); err != nil {
			http.Error(w, "Invalid request body", http.StatusBadRequest)
			return
		}
		log.Printf("Parsed RequestDto: %+v\n", requestDto)

		requestBody, err := json.Marshal(requestDto)
		if err != nil {
			http.Error(w, "Failed to marshal request body", http.StatusInternalServerError)
			return
		}

		targetURL := target + r.URL.Path
		req, err := http.NewRequest(r.Method, targetURL, bytes.NewReader(requestBody))
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}

		req.Header = r.Header

		client := &http.Client{}
		resp, err := client.Do(req)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}
		defer resp.Body.Close()

		for key, values := range resp.Header {
			for _, value := range values {
				w.Header().Add(key, value)
			}
		}

		w.WriteHeader(resp.StatusCode)

		_, err = io.Copy(w, resp.Body)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}
	}
}
