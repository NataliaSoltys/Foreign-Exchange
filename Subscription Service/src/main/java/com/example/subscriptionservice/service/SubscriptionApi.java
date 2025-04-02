package com.example.subscriptionservice.service;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionapi.dto.enums.SubscriptionType;
import com.example.subscriptionservice.model.SubscriptionMapper;
import com.example.subscriptionservice.model.entities.Subscription;
import com.example.subscriptionservice.model.entities.User;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import com.example.subscriptionservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SubscriptionApi {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
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

    public List<SubscriptionDto> getAllSubscriptions() {
        return subscriptionRepository.findAll().stream().map(subscriptionMapper::toDto).toList();
    }

    public List<SubscriptionDto> findBySubscriptionType(SubscriptionType subscriptionType) {
        List<Subscription> foundSubscription = subscriptionRepository.findBySubscriptionType(subscriptionType);
        if (foundSubscription.isEmpty()) {
            throw new RuntimeException("Subscription not found");
        }
        return foundSubscription.stream().map(subscriptionMapper::toDto).toList();
    }

    public List<Subscription> findByUserId(String userId) {
        List<Subscription> foundSubscription = subscriptionRepository.findAllByUserId(UUID.fromString(userId));
        if (foundSubscription.isEmpty()) {
            throw new RuntimeException("Subscription not found");
        }
        return foundSubscription;
    }

    public Subscription addSubscriptionWithExistingUser(SubscriptionDto subscriptionDto) {
        Optional<User> foundUser = userRepository.findById(UUID.fromString(subscriptionDto.getUserId()));
        if (foundUser.isEmpty()) {
            throw new RuntimeException("User not found, cannot save the subscription");
        }
        Subscription subscription = Subscription.builder()
                .user(foundUser.get())
                .currencyCode(subscriptionDto.getCurrencyCode())
                .subscriptionType(subscriptionDto.getSubscriptionType())
                .sellPriceBoundaryValue(subscriptionDto.getSellPriceBoundaryValue())
                .buyPriceBoundaryValue(subscriptionDto.getBuyPriceBoundaryValue())
                .isActive(subscriptionDto.getIsActive())
                .build();
        return subscriptionRepository.save(subscription);
    }
}
