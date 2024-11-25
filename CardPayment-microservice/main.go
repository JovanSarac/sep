package main

import (
	"bytes"
	"fmt"
	"io"
	"net/http"
	"os/exec"
	"runtime"
)

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
	} else {

		go func() {
			err := openBrowser("http://localhost:4202")
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
