package com.example.subscriptionservice.model.entities;

import com.example.subscriptionapi.dto.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subscription")
public class Subscription {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser appUser;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    @Column(name = "subscription_type", nullable = false)
    private SubscriptionType subscriptionType;

    @Column(name = "buy_price_boundary_value", nullable = true)
    private Float buyPriceBoundaryValue;

    @Column(name = "sell_price_boundary_value", nullable = true)
    private Float sellPriceBoundaryValue;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;
}
