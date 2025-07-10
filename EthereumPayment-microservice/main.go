package main

import (
	"encoding/base64"
	"encoding/json"
	"ethereum-payment-microservice/models"
	"fmt"
	"net/http"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
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

func main() {
	database = initDB()
	if database == nil {
		print("FAILED TO CONNECT TO DB")
		return
	}
	http.HandleFunc("/eth", getWalletIds)
	http.HandleFunc("/eth/saveTransaction", saveTransaction)

	fmt.Print("EthService is running on :8084")
	http.ListenAndServe(":8084", nil)
}

func getWalletIds(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var users []models.User
	if err := database.Find(&users).Error; err != nil {
		http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
		return
	}

	// extract wallet IDs
	var walletIds []string
	for _, user := range users {
		decodedWalletIdBytes, err := base64.StdEncoding.DecodeString(user.WalletId)
		if err != nil {
			print("FAILED TO DECODE WALLETID")
			return
		}
		decodedWalletId := string(decodedWalletIdBytes)
		walletIds = append(walletIds, decodedWalletId)
	}

	json.NewEncoder(w).Encode(walletIds)
}

func saveTransaction(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var tx models.Transaction
	if err := json.NewDecoder(r.Body).Decode(&tx); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	fmt.Printf("Received transaction: %+v\n", tx)

	tx.SenderWalletId = base64.StdEncoding.EncodeToString([]byte(tx.SenderWalletId))
	tx.ReceiverWalletId = base64.StdEncoding.EncodeToString([]byte(tx.ReceiverWalletId))

	if err := database.Create(&tx).Error; err != nil {
		http.Error(w, "Failed to save transaction", http.StatusInternalServerError)
		return
	}

	w.WriteHeader(http.StatusCreated)
}
