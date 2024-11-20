package main

import (
	"fmt"
	"net/http"
)

func main() {
	http.HandleFunc("/card", getCard)
	fmt.Println("CardService is running on :8082")
	http.ListenAndServe(":8082", nil)
}

func getCard(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintln(w, "Card data")
}
