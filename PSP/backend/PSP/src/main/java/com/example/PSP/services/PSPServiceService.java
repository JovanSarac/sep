package com.example.PSP.services;

import com.example.PSP.models.PSPService;
import com.example.PSP.repositories.PSPServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PSPServiceService {
    @Autowired
    private final PSPServiceRepository _pspServiceRepository;

    public PSPServiceService(PSPServiceRepository _pspRepository) {
        this._pspServiceRepository = _pspRepository;
    }

    public List<PSPService> getActiveServices() {
        return _pspServiceRepository.findAllActivePSPServices();
    }
}
