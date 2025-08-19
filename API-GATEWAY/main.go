package main

import (
	"bytes"
	"encoding/json"
	"io"
	"log"
	"net/http"

	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
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
	router.HandleFunc("/bank1ValidateRequest", proxy("/bank1ValidateRequest", "http://localhost:8082")).Methods("POST")
	router.HandleFunc("/bank1QRCodeValidateRequest", proxy("/bank1QRCodeValidateRequest", "http://localhost:8083")).Methods("POST")
	router.HandleFunc("/eth", proxy("/eth", "http://localhost:8084")).Methods("GET")
	router.HandleFunc("/eth/saveTransaction", proxy("/eth/saveTransaction", "http://localhost:8084")).Methods("POST")

	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Content-Type", "Authorization"},
		AllowCredentials: true,
	})

	log.Fatal(http.ListenAndServe(":8080", c.Handler(router)))
}

func proxy(path, target string) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		if r.Method == http.MethodOptions {
			w.WriteHeader(http.StatusNoContent)
			return
		}

		var requestBody io.Reader

		log.Println("PROXY")
		if r.Method == http.MethodPost || r.Method == http.MethodPut || r.Method == http.MethodPatch {
			switch path {
			case "/card", "/bank1", "/bank1ValidateRequest":
				log.Println("BANK1VALIDATEREQUEST")
				var requestDto RequestDto
				if err := json.NewDecoder(r.Body).Decode(&requestDto); err != nil {
					log.Println("First if")
					http.Error(w, "Invalid request body", http.StatusBadRequest)
					return
				}
				log.Printf("Parsed RequestDto: %+v\n", requestDto)

				marshaled, err := json.Marshal(requestDto)
				if err != nil {
					http.Error(w, "Failed to marshal request body", http.StatusInternalServerError)
					return
				}
				log.Println("READ THE BODY")
				requestBody = bytes.NewReader(marshaled)
				break
			default:
				//pass the raw body
				bodyBytes, err := io.ReadAll(r.Body)
				if err != nil {
					http.Error(w, "Failed to read request body", http.StatusBadRequest)
					return
				}

				log.Println("Passed raw body")
				requestBody = bytes.NewReader(bodyBytes)
			}
		}

		targetURL := target + r.URL.Path
		req, err := http.NewRequest(r.Method, targetURL, requestBody)
		if err != nil {
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}

		req.Header = r.Header.Clone()

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
		io.Copy(w, resp.Body)
	}
}
