package com.example.subscriptionservice.service;

import com.example.subscriptionservice.model.SubscriptionMapper;
import com.example.subscriptionservice.model.dto.SubscriptionDto;
import com.example.subscriptionservice.model.entities.Subscription;
import com.example.subscriptionservice.model.enums.SubscriptionType;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionApi {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public void addSubscription(SubscriptionDto subscriptionDto) {
        subscriptionRepository.save(subscriptionMapper.toEntity(subscriptionDto));
    }

    public Subscription updateSubscriptionStatus(String subscriptionId, Boolean isActive) {
        Optional<Subscription> foundSubscription = subscriptionRepository.findById(UUID.fromString(subscriptionId));
        if (foundSubscription.isEmpty()) {
            throw new RuntimeException("Subscription not found");
        }
        foundSubscription.get().setIsActive(isActive);
        return subscriptionRepository.save(foundSubscription.get());
    }


    public Optional<Subscription> getSubscription(String subscriptionId) {
        return subscriptionRepository.findById(UUID.fromString(subscriptionId));
    }

    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public List<Subscription> findBySubscriptionType(String subscriptionType) {
        List<Subscription> foundSubscription = subscriptionRepository.findBySubscriptionType(SubscriptionType.valueOf(subscriptionType));
        if (foundSubscription.isEmpty()) {
            throw new RuntimeException("Subscription not found");
        }
        return foundSubscription;
    }
}
