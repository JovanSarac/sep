package com.example.VIVONET.dtos;

import java.util.List;

public class VivonetServiceDto {
    private Long id;
    private String name;
    private String description;
    private String typeUser;  // Pretpostavljam da je ovo tip korisnika, može biti enum
    private String typeService; // Isto za tip servisa, može biti enum
    private List<TariffPlanDto> tariffPlans;

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

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getTypeService() {
        return typeService;
    }

    public void setTypeService(String typeService) {
        this.typeService = typeService;
    }

    public List<TariffPlanDto> getTariffPlans() {
        return tariffPlans;
    }

    public void setTariffPlans(List<TariffPlanDto> tariffPlans) {
        this.tariffPlans = tariffPlans;
    }
}
