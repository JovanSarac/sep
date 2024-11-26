package com.example.PSP.services;

import com.example.PSP.dtos.SubscriptionDto;
import com.example.PSP.models.PSPService;
import com.example.PSP.models.Subscription;
import com.example.PSP.models.User;
import com.example.PSP.repositories.PSPServiceRepository;
import com.example.PSP.repositories.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
        subscriptionDTO.setService(subscription.getService());
        subscriptionDTO.setUserId(subscription.getUser().getId());
        subscriptionDTO.setStartDate(subscription.getStartDate());
        subscriptionDTO.setEndDate(subscription.getEndDate());
        subscriptionDTO.setTotalCost(subscription.getTotalCost());
        subscriptionDTO.setIsActive(subscription.getIsActive());
        subscriptionDTO.setSubscriptionDuration(subscription.getSubscriptionDuration());

        return subscriptionDTO;
    }

    public List<SubscriptionDto> getActiveSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByUserId(userId);
        return subscriptions.stream().map(sub -> {
            SubscriptionDto dto = new SubscriptionDto();
            dto.setId(sub.getId());
            dto.setService(sub.getService());
            dto.setUserId(sub.getUser().getId());
            dto.setStartDate(sub.getStartDate());
            dto.setEndDate(sub.getEndDate());
            dto.setTotalCost(sub.getTotalCost());
            dto.setIsActive(sub.getIsActive());
            dto.setSubscriptionDuration(sub.getSubscriptionDuration());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByUserId(userId);
        return subscriptions.stream().map(sub -> {
            SubscriptionDto dto = new SubscriptionDto();
            dto.setId(sub.getId());
            dto.setService(sub.getService());
            dto.setUserId(sub.getUser().getId());
            dto.setStartDate(sub.getStartDate());
            dto.setEndDate(sub.getEndDate());
            dto.setTotalCost(sub.getTotalCost());
            dto.setIsActive(sub.getIsActive());
            dto.setSubscriptionDuration(sub.getSubscriptionDuration());
            return dto;
        }).collect(Collectors.toList());
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateSubscriptionStatus() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Subscription subscription : subscriptions) {
            if (subscription.getEndDate() != null) {
                boolean isActive = subscription.getEndDate().isAfter(today);
                if (!subscription.getIsActive().equals(isActive)) {
                    subscription.setIsActive(isActive);
                    subscriptionRepository.save(subscription);
                }
            }
        }
    }

    public void updateSubscription(SubscriptionDto subscriptionDTO) {
        Subscription subscription = subscriptionRepository.findById(subscriptionDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Subscription with ID " + subscriptionDTO.getId() + " does not exist."));

        subscription.setIsActive(subscriptionDTO.getIsActive());
        subscription.setEndDate(subscriptionDTO.getEndDate());
        subscription.setTotalCost(subscriptionDTO.getTotalCost());
        subscription.setSubscriptionDuration(subscriptionDTO.getSubscriptionDuration());
        subscriptionRepository.save(subscription);

    }
}

