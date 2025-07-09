package models

type Transaction struct {
	Id               int     `gorm:"primaryKey" json:"id"`
	SenderWalletId   string  `gorm:"column:senderWalletId" json:"senderWalletId"`
	ReceiverWalletId string  `gorm:"column:receiverWalletId" json:"receiverWalletId"`
	Amount           float64 `gorm:"column:amount" json:"amount"`
	TransactionHash  string  `gorm:"column:transactionHash" json:"transactionHash"`
}
