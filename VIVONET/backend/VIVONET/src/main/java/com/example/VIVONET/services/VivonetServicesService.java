package com.example.VIVONET.services;

import com.example.VIVONET.models.TariffPlan;
import com.example.VIVONET.models.VivonetService;
import com.example.VIVONET.repositories.VivonetServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.VIVONET.dtos.VivonetServiceDto;
import com.example.VIVONET.dtos.TariffPlanDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VivonetServicesService {
    private final VivonetServicesRepository vivonetServicesRepository;

    @Autowired
    public VivonetServicesService(VivonetServicesRepository vivonetServicesRepository) {
        this.vivonetServicesRepository = vivonetServicesRepository;
    }

    public List<VivonetServiceDto> getMobileServicesBussines() {
        // Dobijanje listu VivonetService entiteta iz repozitorijuma
        List<VivonetService> services = vivonetServicesRepository.getMobileServicesBussines();
        // Mapiranje listu VivonetService objekata u DTO objekte
        return services.stream()
                .map(this::mapToDTO)  // Mapira svaki VivonetService u VivonetServiceDto
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getMobileServicesPersonal() {
        List<VivonetService> services = vivonetServicesRepository.getMobileServicesPersonal();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getLandlineServicesPersonal() {
        List<VivonetService> services = vivonetServicesRepository.getLandlineServicesPersonal();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getLandlineServicesBussines() {
        List<VivonetService> services = vivonetServicesRepository.getLandlineServicesBussines();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getInternetServicesPersonal() {
        List<VivonetService> services = vivonetServicesRepository.getInternetServicesPersonal();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getInternetServicesBussines() {
        List<VivonetService> services = vivonetServicesRepository.getInternetServicesBussines();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getTVServicesPersonal() {
        List<VivonetService> services = vivonetServicesRepository.getTVServicesPersonal();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<VivonetServiceDto> getTVServicesBussines() {
        List<VivonetService> services = vivonetServicesRepository.getTVServicesBussines();
        return services.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public VivonetServiceDto mapToDTO(VivonetService service) {
        VivonetServiceDto dto = new VivonetServiceDto();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setDescription(service.getDescription());
        dto.setTypeUser(service.getTypeUser().toString());
        dto.setTypeService(service.getTypeService().toString());
        dto.setTariffPlans(service.getTariffPlans().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private TariffPlanDto mapToDTO(TariffPlan tariffPlan) {
        TariffPlanDto dto = new TariffPlanDto();
        dto.setId(tariffPlan.getId());
        dto.setName(tariffPlan.getName());
        dto.setDescription(tariffPlan.getDescription());
        dto.setPrice(tariffPlan.getPrice());
        dto.setDurationContract(tariffPlan.getDurationContract());
        dto.setInternetGb(tariffPlan.getInternetGb());
        dto.setNumberMinute(tariffPlan.getNumberMinute());
        dto.setNumberSMS(tariffPlan.getNumberSMS());
        dto.setNumberChannel(tariffPlan.getNumberChannel());
        dto.setHdChannel(tariffPlan.getHdChannel());
        dto.setSpeedInternetMbps(tariffPlan.getSpeedInternetMbps());
        return dto;
    }
}
