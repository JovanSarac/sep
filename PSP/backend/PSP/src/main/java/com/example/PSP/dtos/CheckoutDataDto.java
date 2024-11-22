package com.example.PSP.dtos;

public class CheckoutDataDto {
    private Long userId;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private CartDto cart;
    private Long webShopId;
    private String webShopName;
    private String webShopUrl;

    public CartDto getCart() {
        return cart;
    }

    public void setCart(CartDto cart) {
        this.cart = cart;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getWebShopId() {
        return webShopId;
    }

    public void setWebShopId(Long webShopId) {
        this.webShopId = webShopId;
    }

    public String getWebShopName() {
        return webShopName;
    }

    public void setWebShopName(String webShopName) {
        this.webShopName = webShopName;
    }

    public String getWebShopUrl() {
        return webShopUrl;
    }

    public void setWebShopUrl(String webShopUrl) {
        this.webShopUrl = webShopUrl;
    }
}
