package com.example.subscriptionservice.web;

import com.example.subscriptionservice.model.dto.SubscriptionDto;
import com.example.subscriptionservice.model.entities.Subscription;
import com.example.subscriptionservice.service.SubscriptionApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionApi subscriptionApi;

    private static Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @PostMapping
    @RequestMapping("/add")
    public ResponseEntity<Object> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        try {
            subscriptionApi.addSubscription(subscriptionDto);
            logger.info("Created subscription with data: {}", subscriptionDto.toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{subscriptionId}")
    public ResponseEntity<Object> updateSubscriptionStatus(@PathVariable String subscriptionId, @RequestBody Boolean isActive) {
        Optional<Subscription> subscription = Optional.ofNullable(subscriptionApi.updateSubscriptionStatus(subscriptionId, isActive));
        return subscription.map(s -> {
                    logger.info("Subscription updated: {}", s);
                    return ResponseEntity.status(HttpStatus.OK).build();
                })
                .orElseGet(() -> {
                    logger.info("No subscription found for id {}", subscriptionId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                });
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable String subscriptionId) {
        Optional<Subscription> subscription = subscriptionApi.getSubscription(subscriptionId);
        if (subscription.isEmpty()) {
            logger.info("No subscription found for id {}", subscriptionId);
            return ResponseEntity.badRequest().build();
        }
        logger.info("Fetched subscription with id: {}", subscriptionId);
        return ResponseEntity.ok(subscription.get());
    }

    @GetMapping
    @RequestMapping("/all")
    public ResponseEntity<List<Subscription>> getSubscriptions() {
        List<Subscription> subscriptions = subscriptionApi.getAllSubscriptions();
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Fetched {} subscriptions.", subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<Subscription>> getSubscriptionsByType(@RequestParam String subscriptionType) {
        try {
            List<Subscription> subscriptions = subscriptionApi.findBySubscriptionType(subscriptionType);
            logger.info("Fetched {} subscriptions of type {}", subscriptions.size(), subscriptionType);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            logger.error("Error fetching subscriptions by type {}: {}", subscriptionType, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}