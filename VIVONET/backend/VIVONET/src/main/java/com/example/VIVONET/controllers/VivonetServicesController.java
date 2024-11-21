package com.example.VIVONET.controllers;

import com.example.VIVONET.dtos.VivonetServiceDto;
import com.example.VIVONET.models.VivonetService;
import com.example.VIVONET.services.VivonetServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vivonetServices")
public class VivonetServicesController {

    private final VivonetServicesService vivonetServicesService;

    @Autowired
    public VivonetServicesController(VivonetServicesService vivonetServicesService) {
        this.vivonetServicesService = vivonetServicesService;
    }

    // Mobile Services for Business
    @GetMapping("/mobile/business")
    public List<VivonetServiceDto> getMobileServicesBussines() {
        return vivonetServicesService.getMobileServicesBussines();
    }

    // Mobile Services for Personal
    @GetMapping("/mobile/personal")
    public List<VivonetServiceDto> getMobileServicesPersonal() {
        return vivonetServicesService.getMobileServicesPersonal();
    }

    // Landline Services for Business
    @GetMapping("/landline/business")
    public List<VivonetServiceDto> getLandlineServicesBussines() {
        return vivonetServicesService.getLandlineServicesBussines();
    }

    // Landline Services for Personal
    @GetMapping("/landline/personal")
    public List<VivonetServiceDto> getLandlineServicesPersonal() {
        return vivonetServicesService.getLandlineServicesPersonal();
    }

    // Internet Services for Business
    @GetMapping("/internet/business")
    public List<VivonetServiceDto> getInternetServicesBussines() {
        return vivonetServicesService.getInternetServicesBussines();
    }

    // Internet Services for Personal
    @GetMapping("/internet/personal")
    public List<VivonetServiceDto> getInternetServicesPersonal() {
        return vivonetServicesService.getInternetServicesPersonal();
    }

    // TV Services for Business
    @GetMapping("/tv/business")
    public List<VivonetServiceDto> getTVServicesBussines() {
        return vivonetServicesService.getTVServicesBussines();
    }

    // TV Services for Personal
    @GetMapping("/tv/personal")
    public List<VivonetServiceDto> getTVServicesPersonal() {
        return vivonetServicesService.getTVServicesPersonal();
    }
}