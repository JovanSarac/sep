package com.example.PSP.dtos;

public class AccessToken {
    private Long id;
    private String accessToken;
    public void setId(Long id) {
        this.id = id;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public AccessToken(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public Long getId() {
        return id;
    }
}
