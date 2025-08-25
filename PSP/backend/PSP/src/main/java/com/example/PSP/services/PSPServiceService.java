package com.example.PSP.services;

import com.example.PSP.models.PSPService;
import com.example.PSP.repositories.PSPServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PSPServiceService {
    @Autowired
    private final PSPServiceRepository _pspServiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(PSPServiceService.class);

    public PSPServiceService(PSPServiceRepository _pspRepository) {
        this._pspServiceRepository = _pspRepository;
    }

    public List<PSPService> getActiveServices() {
        logger.info("Retrieving all active PSP services");
        List<PSPService> allPspServices = _pspServiceRepository.findAllActivePSPServices();

        return allPspServices;
    }

    public PSPService save(PSPService service){
        return _pspServiceRepository.save(service);
    }

    public void removeById(Long id){
        _pspServiceRepository.removeById(id);
    }

    public List<PSPService> findAll(){
        return _pspServiceRepository.findAll();
    }
}
