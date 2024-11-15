package com.example.PSP.repositories;

import com.example.PSP.models.PSPService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PSPServiceRepository extends JpaRepository<PSPService, Long> {

    @Query("SELECT pm FROM PSPService pm WHERE pm.isActive = true")
    List<PSPService> findAllActivePSPServices();
}
