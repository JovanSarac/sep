package com.example.PSP.dtos;

public class UserInfoDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String webURL;
    private String copmanyAddress;

    public UserInfoDto(Long id, String firstName, String lastName, String email, String companyName, String webURL, String copmanyAddress) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.companyName = companyName;
        this.webURL = webURL;
        this.copmanyAddress = copmanyAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getCopmanyAddress() {
        return copmanyAddress;
    }

    public void setCopmanyAddress(String copmanyAddress) {
        this.copmanyAddress = copmanyAddress;
    }
}
