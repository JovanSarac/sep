package com.example.PSP.dtos;

public class AccessToken {
    private Long id;
    private String accessToken;
    private String refreshToken;

    public void setId(Long id) {
        this.id = id;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public AccessToken(Long id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public Long getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
