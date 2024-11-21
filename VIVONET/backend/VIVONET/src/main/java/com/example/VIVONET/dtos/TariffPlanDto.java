package com.example.VIVONET.dtos;

public class TariffPlanDto {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int durationContract;
    private Integer internetGb;
    private Integer numberMinute;
    private Integer numberSMS;
    private Integer numberChannel;
    private Boolean hdChannel;
    private Integer speedInternetMbps;

    // Getteri i setteri

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDurationContract() {
        return durationContract;
    }

    public void setDurationContract(int durationContract) {
        this.durationContract = durationContract;
    }

    public Integer getInternetGb() {
        return internetGb;
    }

    public void setInternetGb(Integer internetGb) {
        this.internetGb = internetGb;
    }

    public Integer getNumberMinute() {
        return numberMinute;
    }

    public void setNumberMinute(Integer numberMinute) {
        this.numberMinute = numberMinute;
    }

    public Integer getNumberSMS() {
        return numberSMS;
    }

    public void setNumberSMS(Integer numberSMS) {
        this.numberSMS = numberSMS;
    }

    public Integer getNumberChannel() {
        return numberChannel;
    }

    public void setNumberChannel(Integer numberChannel) {
        this.numberChannel = numberChannel;
    }

    public Boolean getHdChannel() {
        return hdChannel;
    }

    public void setHdChannel(Boolean hdChannel) {
        this.hdChannel = hdChannel;
    }

    public Integer getSpeedInternetMbps() {
        return speedInternetMbps;
    }

    public void setSpeedInternetMbps(Integer speedInternetMbps) {
        this.speedInternetMbps = speedInternetMbps;
    }
}
