package main

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"strings"

	"github.com/coreos/go-oidc"
	"github.com/google/uuid"
	"github.com/gorilla/mux"
	"github.com/rs/cors"
)

var verifier *oidc.IDTokenVerifier

func initKeycloak() {
	ctx := context.Background()
	provider, err := oidc.NewProvider(ctx, "http://localhost:8080/realms/sep-realm")
	if err != nil {
		panic(err)
	}

	verifier = provider.Verifier(&oidc.Config{
		ClientID: "sep-api-gateway",
	})
}

func authMiddleware(next http.Handler) http.Handler {

	publicRoutes := []string{
		"/eth",
		"/eth/saveTransaction",
	}

	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {

		for _, route := range publicRoutes {
			if r.URL.Path == route {
				next.ServeHTTP(w, r)
				return
			}
		}

		authHeader := r.Header.Get("Authorization")
		if authHeader == "" || !strings.HasPrefix(authHeader, "Bearer ") {
			http.Error(w, "Missing or invalid Authorization header", http.StatusUnauthorized)
			fmt.Sprintf("Invalid token " + authHeader)
			return
		}

		token := strings.TrimPrefix(authHeader, "Bearer ")

		ctx := r.Context()
		idToken, err := verifier.Verify(ctx, token)
		if err != nil {
			http.Error(w, "Invalid token", http.StatusUnauthorized)
			return
		}

		var claims struct {
			Aud []string `json:"aud"`
		}

		if err := idToken.Claims(&claims); err != nil {
			http.Error(w, "Failed to parse claims", http.StatusUnauthorized)
			return
		}

		validAud := false
		for _, a := range claims.Aud {
			if a == "sep-api-gateway" {
				validAud = true
				break
			}
		}

		if !validAud {
			http.Error(w, "Token not intended for this service", http.StatusForbidden)
			return
		}

		// token je validan → pusti dalje
		next.ServeHTTP(w, r)
	})
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

func getServiceURL(serviceName string) (string, error) {
	resp, err := http.Get("http://localhost:8500/v1/catalog/service/" + serviceName + "?passing=true")
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	var services []struct {
		ServiceAddress string
		ServicePort    int
	}
	if err := json.NewDecoder(resp.Body).Decode(&services); err != nil {
		return "", err
	}

	if len(services) == 0 {
		return "", fmt.Errorf("service %s not found", serviceName)
	}
	return fmt.Sprintf("http://%s:%d", services[0].ServiceAddress, services[0].ServicePort), nil
}

func main() {
	initKeycloak()

	// register the handler function with the server

	router := mux.NewRouter()
	router.Use(authMiddleware)
	router.HandleFunc("/card", func(w http.ResponseWriter, r *http.Request) {
		url, err := getServiceURL("card")
		if err != nil {
			http.Error(w, "Service unavailable", http.StatusServiceUnavailable)
			return
		}
		proxy("/card", url)(w, r)
	}).Methods("GET")
	router.HandleFunc("/bank1ValidateRequest", func(w http.ResponseWriter, r *http.Request) {
		url, err := getServiceURL("card")
		if err != nil {
			http.Error(w, "Service unavailable", http.StatusServiceUnavailable)
			return
		}
		proxy("/bank1ValidateRequest", url)(w, r)
	}).Methods("POST")
	router.HandleFunc("/bank1QRCodeValidateRequest", func(w http.ResponseWriter, r *http.Request) {
		url, err := getServiceURL("qrCode")
		if err != nil {
			http.Error(w, "Service unavailable", http.StatusServiceUnavailable)
			return
		}
		log.Println("URL QR REQUEST")
		log.Println(url)
		proxy("/bank1QRCodeValidateRequest", url)(w, r)
	}).Methods("POST")
	router.HandleFunc("/eth", func(w http.ResponseWriter, r *http.Request) {
		url, err := getServiceURL("eth")
		if err != nil {
			http.Error(w, "Service unavailable", http.StatusServiceUnavailable)
			return
		}
		proxy("/eth", url)(w, r)
	}).Methods("GET")
	router.HandleFunc("/eth/saveTransaction", func(w http.ResponseWriter, r *http.Request) {
		url, err := getServiceURL("eth")
		if err != nil {
			http.Error(w, "Service unavailable", http.StatusServiceUnavailable)
			return
		}
		proxy("/eth/saveTransaction", url)(w, r)
	}).Methods("POST")

	c := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowedHeaders:   []string{"Content-Type", "Authorization"},
		AllowCredentials: true,
	})

	//log.Fatal(http.ListenAndServeTLS(":8080", "apigateway.crt", "apigateway.key", c.Handler(router)))
	log.Fatal(http.ListenAndServeTLS(":9001", "apigateway.crt", "apigateway.key", c.Handler(router)))
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
			case "/card", "/bank1", "/bank1ValidateRequest", "/bank1QRCodeValidateRequest":
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
		log.Println("TARGETURL")
		log.Println(targetURL)
		req, err := http.NewRequest(r.Method, targetURL, requestBody)
		if err != nil {
			log.Println("Prvi error")
			log.Println(err)
			http.Error(w, err.Error(), http.StatusBadGateway)
			return
		}

		log.Println("HEADER")

		req.Header = r.Header.Clone()
		log.Println(req.Header)
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
		log.Println(resp)
		io.Copy(w, resp.Body)
	}
}
