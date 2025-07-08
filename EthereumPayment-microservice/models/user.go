package models

type User struct {
	Id        int    `gorm:"column:firstName;primaryKey" json:"id"`
	FirstName string `gorm:"column:firstName" json:"firstName"`
	LastName  string `gorm:"column:lastName" json:"lastName"`
	WalletId  string `gorm:"column:walletId" json:"walletId"`
}
