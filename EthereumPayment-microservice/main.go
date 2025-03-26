package main

import (
	"context"
	"fmt"
	"log"

	"github.com/ethereum/go-ethereum/common"
	"github.com/ethereum/go-ethereum/ethclient"
)

func main() {
	//infura service for etherium nodes for mainnet, testnet..
	conn, err := ethclient.Dial("https://mainnet.infura.io")
	if err != nil {
		log.Fatalf("Error connecting to eth ", err)
	}

	ctx := context.Background()

	//get transaction from hash (found on etherscan.io)
	//tx - transaction
	//pending - true/false, depending on if the transaction is pending
	//error
	tx, pending, _ := conn.TransactionByHash(ctx, common.HexToHash("0xbc1ccf8cfc35b43e4bb5d5a1e270887ac43acf092aaa504172fdc3d7bcd96514"))
	if !pending {
		fmt.Println("Transakcija: ", tx)
	}
}
