package com.example.subscriptionservice.web;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionapi.dto.enums.SubscriptionType;
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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionApi subscriptionApi;

    private static Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @PostMapping
    @RequestMapping("/addWithNewUser")
    public ResponseEntity<Object> addSubscriptionWithNewUser(@RequestBody SubscriptionDto subscriptionDto) {
        try {
            subscriptionApi.addSubscription(subscriptionDto);
            logger.info("Created subscription with data: {}", subscriptionDto.toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @RequestMapping("/addWithExistingUser")
    public ResponseEntity<Object> addSubscriptionWithExistingUser(@RequestBody SubscriptionDto subscriptionDto) {
        try {
            Subscription subscription = subscriptionApi.addSubscriptionWithExistingUser(subscriptionDto);
            logger.info("Created subscription with data: {}", subscription.toString());
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
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionApi.getAllSubscriptions();
        if (subscriptions.isEmpty()) {
            logger.info("No subscriptions found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Fetched {} subscriptions.", subscriptions.size());
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/by-type")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByType(@RequestParam SubscriptionType subscriptionType) {
        try {
            List<SubscriptionDto> subscriptions = subscriptionApi.findBySubscriptionType(subscriptionType);
            logger.info("Fetched {} subscriptions of type {}", subscriptions.size(), subscriptionType);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            logger.error("Error fetching subscriptions by type {}: {}", subscriptionType, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/by-user")
    public ResponseEntity<List<Subscription>> getSubscriptionsByType(@RequestBody String userId) {
        try {
            List<Subscription> subscriptions = subscriptionApi.findByUserId(userId);
            logger.info("Fetched {} subscriptions with last name {}, id: {}", subscriptions.size(), subscriptions.get(0).getUser().getLastName(), userId);
            return ResponseEntity.ok(subscriptions);
        } catch (Exception e) {
            logger.error("Error fetching subscriptions: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/grouped-by-type")
    public ResponseEntity<Map<String, List<SubscriptionDto>>> getAllSubscriptionsGroupedByType() {
        Map<String, List<SubscriptionDto>> subscriptions = subscriptionApi.getAllSubscriptions()
                .stream()
                .collect(Collectors.groupingBy(sub -> sub.getSubscriptionType().name()));
        logger.info("Grouped subscriptions by type: {}", subscriptions);
        return ResponseEntity.ok(subscriptions);
    }
}