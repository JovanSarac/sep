package com.example.VIVONET.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class TariffPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int durationContract;

    private Integer internetGb;
    private Integer numberMinute;
    private Integer numberSMS;
    private Integer numberChannel;
    private Boolean hdChannel;
    private Integer speedInternetMbps;


    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private VivonetService vivonetService;

    public TariffPlan() {
    }

    public TariffPlan(String name, String description, double price, int durationContract, Integer internetGb,
                      Integer numberMinute, Integer numberSMS, Integer numberChannel, Boolean hdChannel, Integer speedInternetMbps, VivonetService vivonetService) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.durationContract = durationContract;
        this.internetGb = internetGb;
        this.numberMinute = numberMinute;
        this.numberSMS = numberSMS;
        this.numberChannel = numberChannel;
        this.hdChannel = hdChannel;
        this.speedInternetMbps = speedInternetMbps;
        this.vivonetService = vivonetService;
    }

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getDurationContract() { return durationContract; }
    public void setDurationContract(int durationContract) { this.durationContract = durationContract; }

    public Integer getInternetGb() { return internetGb; }
    public void setInternetGb(Integer internetGb) { this.internetGb = internetGb; }

    public Integer getNumberMinute() { return numberMinute; }
    public void setNumberMinute(Integer numberMinute) { this.numberMinute = numberMinute; }

    public Integer getNumberSMS() { return numberSMS; }
    public void setNumberSMS(Integer numberSMS) { this.numberSMS = numberSMS; }

    public Integer getNumberChannel() { return numberChannel; }
    public void setNumberChannel(Integer numberChannel) { this.numberChannel = numberChannel; }

    public Boolean getHdChannel() { return hdChannel; }
    public void setHdChannel(Boolean hdChannel) { this.hdChannel = hdChannel; }

    public Integer getSpeedInternetMbps() { return speedInternetMbps; }
    public void setSpeedInternetMbps(Integer speedInternetMbps) { this.speedInternetMbps = speedInternetMbps; }

    public VivonetService getService() { return vivonetService; }
    public void setService(VivonetService service) { this.vivonetService = service; }
}