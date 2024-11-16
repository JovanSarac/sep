package com.example.PSP.services;

import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Subscription;
import com.example.PSP.models.User;
import com.example.PSP.repositories.PSPServiceRepository;
import com.example.PSP.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PSPServiceRepository pspServiceRepository;

    public SubscriptionDto createSubscription(User user, PSPService service, Integer subscriptionDuration) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setService(service);
        subscription.setStartDate(LocalDate.now());
        subscription.setSubscriptionDuration(subscriptionDuration);
        subscription.calculateEndDate();

        Double totalCost = subscription.calculateTotalCost();
        subscription.setTotalCost(totalCost);

        subscription.setIsActive(true);

        subscriptionRepository.save(subscription);

        SubscriptionDto subscriptionDTO = new SubscriptionDto();
        subscriptionDTO.setId(subscription.getId());
        subscriptionDTO.setServiceId(subscription.getService().getId());
        subscriptionDTO.setUserId(subscription.getUser().getId());
        subscriptionDTO.setStartDate(subscription.getStartDate());
        subscriptionDTO.setEndDate(subscription.getEndDate());
        subscriptionDTO.setTotalCost(subscription.getTotalCost());
        subscriptionDTO.setIsActive(subscription.getIsActive());
        subscriptionDTO.setSubscriptionDuration(subscription.getSubscriptionDuration());

        return subscriptionDTO;
    }

    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByUserId(userId);
        return subscriptions.stream().map(sub -> {
            SubscriptionDto dto = new SubscriptionDto();
            dto.setId(sub.getId());
            dto.setServiceId(sub.getService().getId());
            dto.setUserId(sub.getUser().getId()); // Samo ID korisnika
            dto.setStartDate(sub.getStartDate());
            dto.setEndDate(sub.getEndDate());
            dto.setTotalCost(sub.getTotalCost());
            dto.setIsActive(sub.getIsActive());
            dto.setSubscriptionDuration(sub.getSubscriptionDuration());
            return dto;
        }).collect(Collectors.toList());
    }
}

