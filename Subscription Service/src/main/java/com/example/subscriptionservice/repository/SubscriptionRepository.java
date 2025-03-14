package com.example.subscriptionservice.repository;

import com.example.subscriptionservice.model.entities.Subscription;
import com.example.subscriptionservice.model.enums.SubscriptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    List<Subscription> findBySubscriptionType(SubscriptionType subscriptionType);

    List<Subscription> findAllByUserId(UUID uuid);
}
