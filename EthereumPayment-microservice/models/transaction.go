package models

type Transaction struct {
	Id         int     `gorm:"column:firstName;primaryKey" json:"id"`
	SenderId   int     `gorm:"column:senderId" json:"senderId"`
	ReceiverId int     `gorm:"column:receiverId" json:"receiverId"`
	Amount     float64 `gorm:"column:amount" json:"amount"`
}
