package com.example.VIVONET.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="Services")
public class VivonetService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeUserForService typeUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeService typeService;

    @OneToMany(mappedBy = "vivonetService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TariffPlan> tariffPlans;

    public VivonetService() {
    }

    public VivonetService(String name, String description, TypeUserForService typeUser, TypeService typeService, List<TariffPlan> tariffPlans) {
        this.name = name;
        this.description = description;
        this.typeUser = typeUser;
        this.typeService = typeService;
        this.tariffPlans = tariffPlans;
    }

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TypeUserForService getTypeUser() { return typeUser; }
    public void setTypeUser(TypeUserForService typeUser) { this.typeUser = typeUser; }

    public TypeService getTypeService() { return typeService; }
    public void setTypeService(TypeService typeService) { this.typeService = typeService; }

    public List<TariffPlan> getTariffPlans() { return tariffPlans; }
    public void setTariffPlans(List<TariffPlan> tariffPlans) { this.tariffPlans = tariffPlans; }

}
