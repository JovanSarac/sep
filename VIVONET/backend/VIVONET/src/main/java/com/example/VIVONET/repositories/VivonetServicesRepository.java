package com.example.VIVONET.repositories;

import com.example.VIVONET.models.VivonetService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VivonetServicesRepository extends JpaRepository<VivonetService, Long> {
    // Mobile Services for Business
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'MOBILE' AND s.typeUser = 'BUSINESS'")
    List<VivonetService> getMobileServicesBussines();

    // Mobile Services for Personal
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'MOBILE' AND s.typeUser = 'PERSONAL'")
    List<VivonetService> getMobileServicesPersonal();

    // Landline Services for Business
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'LANDLINE' AND s.typeUser = 'BUSINESS'")
    List<VivonetService> getLandlineServicesBussines();

    // Landline Services for Personal
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'LANDLINE' AND s.typeUser = 'PERSONAL'")
    List<VivonetService> getLandlineServicesPersonal();

    // Internet Services for Business
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'INTERNET' AND s.typeUser = 'BUSINESS'")
    List<VivonetService> getInternetServicesBussines();

    // Internet Services for Personal
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'INTERNET' AND s.typeUser = 'PERSONAL'")
    List<VivonetService> getInternetServicesPersonal();

    // TV Services for Business
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'TV' AND s.typeUser = 'BUSINESS'")
    List<VivonetService> getTVServicesBussines();

    // TV Services for Personal
    @Query("SELECT s FROM VivonetService s WHERE s.typeService = 'TV' AND s.typeUser = 'PERSONAL'")
    List<VivonetService> getTVServicesPersonal();
}
