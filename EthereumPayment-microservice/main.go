package main

import (
	"encoding/json"
	"fmt"
	"net/http"

	"gorm.io/driver/postgres"
	"gorm.io/gorm"
)

func initDB() *gorm.DB {
	connStr := "user=postgres dbname=EthPayment password=super sslmode=disable"
	db, err := gorm.Open(postgres.Open(connStr), &gorm.Config{})
	if err != nil {
		panic(err)
	}
	return db
}

func main() {
	database := initDB()
	if database == nil {
		print("FAILED TO CONNECT TO DB")
		return
	}
	http.HandleFunc("/eth", getWalletIds)
	fmt.Print("EthService is running on :8083")
	http.ListenAndServe(":8083", nil)
}

func getWalletIds(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	data := []string{"123", "456"}
	json.NewEncoder(w).Encode(data)
}
