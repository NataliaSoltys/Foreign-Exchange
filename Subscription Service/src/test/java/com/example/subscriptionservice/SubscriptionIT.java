package com.example.subscriptionservice;

import com.example.subscriptionapi.dto.SubscriptionDto;
import com.example.subscriptionapi.dto.enums.SubscriptionType;
import com.example.subscriptionservice.model.entities.AppUser;
import com.example.subscriptionservice.model.entities.Subscription;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import com.example.subscriptionservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubscriptionIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        subscriptionRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String baseUrl(String path) {
        return "/subscriptions" + path;
    }

    @Test
    void shouldAddSubscriptionWithNewUser() {
        // given: there are subscription data prepared to be saved
        SubscriptionDto dto = createSampleDto(true, "");

        // when: user tries to save the subscription
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl("/addWithNewUser"), dto, Void.class);

        // then: subscription is being saved
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, subscriptionRepository.count());
    }

    @Test
    void shouldAddSubscriptionWithExistingUser() {
        // given: there are subscription data prepared to be saved
        AppUser appUser = userRepository.save(getUser("Jan", "Kowalski", "ejan@gmail.com", "123123123", "Kozlowek 3", "Polska"));
        SubscriptionDto dto = createSampleDto(false, appUser.getId().toString());

        // when: user tries to save the subscription
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl("/addWithExistingUser"), dto, Void.class);

        // then: subscription is being saved
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, subscriptionRepository.count());
    }

    @Test
    void shouldUpdateSubscriptionStatus() {
        // given: there are already saved subscriptions data in DB with active status
        AppUser user = userRepository.save(getUser("Anna", "Nowak", "anna@example.com", "123456789", "Adres", "PL"));
        Subscription subscription = subscriptionRepository.save(Subscription.builder()
                .isActive(true)
                .subscriptionType(SubscriptionType.NEW_CURRENCY_RATE)
                .currencyCode("USD")
                .appUser(user)
                .build());

        // when: user disables the subscription
        restTemplate.put(baseUrl("/" + subscription.getId()), false);

        // then: subscription is disabled
        Subscription updated = subscriptionRepository.findById(subscription.getId()).orElseThrow();
        assertFalse(updated.getIsActive());
    }

    @Test
    void shouldReturnSingleSubscriptionById() {
        // given: there is already saved subscription in DB
        AppUser user = userRepository.save(getUser("Anna", "Nowak", "anna@example.com", "123456789", "Adres", "PL"));
        Subscription subscription = subscriptionRepository.save(Subscription.builder()
                .isActive(true)
                .subscriptionType(SubscriptionType.NEW_CURRENCY_RATE)
                .currencyCode("EUR")
                .appUser(user)
                .build());

        // when: user wants to get the subscription
        ResponseEntity<Subscription> response = restTemplate.getForEntity(baseUrl("/" + subscription.getId()), Subscription.class);

        // then: subscription is fetched
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(subscription.getId(), response.getBody().getId());
    }

    @Test
    void shouldReturnAllSubscriptions() {
        // given: there are already saved subscriptions in DB
        AppUser appUser = userRepository.save(getUser("Ala", "Test", "ala@example.com", "111", "ul. Testowa", "PL"));
        subscriptionRepository.saveAll(List.of(
                Subscription.builder().appUser(appUser).currencyCode("USD").subscriptionType(SubscriptionType.NEW_CURRENCY_RATE).isActive(true).build(),
                Subscription.builder().appUser(appUser).currencyCode("EUR").subscriptionType(SubscriptionType.MAXIMUM_VALUE_EXCEEDED).isActive(false).build()
        ));

        // when: user wants to get all subscriptions
        ResponseEntity<SubscriptionDto[]> response = restTemplate.getForEntity(baseUrl("/all"), SubscriptionDto[].class);

        // then: subscriptions are fetched
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void shouldReturnSubscriptionsByType() {
        // given: there are already saved subscriptions in DB with specific type
        AppUser appUser = userRepository.save(getUser("Ala", "Test", "ala@example.com", "111", "ul. Testowa", "PL"));
        subscriptionRepository.save(Subscription.builder()
                .appUser(appUser)
                .currencyCode("CHF")
                .subscriptionType(SubscriptionType.MAXIMUM_VALUE_EXCEEDED)
                .isActive(true)
                .build());

        // when: user wants to get subscriptions by type
        ResponseEntity<SubscriptionDto[]> response = restTemplate.getForEntity(
                baseUrl("/by-type?subscriptionType=MAXIMUM_VALUE_EXCEEDED"), SubscriptionDto[].class);

        // then: subscriptions are fetched
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void shouldReturnSubscriptionsByUser() {
        // given: there are subscription and user already saved in DB
        AppUser appUser = userRepository.save(getUser("Ola", "Nowak", "ola@example.com", "222", "ul. Próba", "PL"));
        subscriptionRepository.save(Subscription.builder()
                .appUser(appUser)
                .currencyCode("GBP")
                .subscriptionType(SubscriptionType.NEW_CURRENCY_RATE)
                .isActive(true)
                .build());

        // when: user wants to get all subscription created by him
        String url = baseUrl("/by-user?userId=" + appUser.getId());

        ResponseEntity<Subscription[]> response = restTemplate.getForEntity(url, Subscription[].class);

        // then: subscriptions are fetched
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }


    @Test
    void shouldReturnGroupedSubscriptions() {
        // given: there are subscriptions already saved in DB
        AppUser appUser = userRepository.save(getUser("Ola", "Nowak", "ola@example.com", "222", "ul. Próba", "PL"));
        subscriptionRepository.saveAll(List.of(
                Subscription.builder().appUser(appUser).currencyCode("USD").subscriptionType(SubscriptionType.NEW_CURRENCY_RATE).isActive(true).build(),
                Subscription.builder().appUser(appUser).currencyCode("USD").subscriptionType(SubscriptionType.NEW_CURRENCY_RATE).isActive(true).build(),
                Subscription.builder().appUser(appUser).currencyCode("USD").subscriptionType(SubscriptionType.MINIMUM_VALUE_EXCEEDED).isActive(true).build()
        ));

        // when: notification service wants to get all subscriptions grouped by type
        ResponseEntity<Map> response = restTemplate.getForEntity(baseUrl("/grouped-by-type"), Map.class);

        // then: subscriptions are fetched
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("NEW_CURRENCY_RATE"));
        assertEquals(2, ((List<?>) response.getBody().get("NEW_CURRENCY_RATE")).size());
    }

    private AppUser getUser(String firstName, String lastName, String email, String phoneNumber, String address, String country) {
        return AppUser.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(address)
                .country(country)
                .build();
    }

    private SubscriptionDto createSampleDto(boolean isUserNew, String userId) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setIsActive(true);
        dto.setSubscriptionType(SubscriptionType.NEW_CURRENCY_RATE);
        dto.setCurrencyCode("USD");
        dto.setSellPriceBoundaryValue(4.5f);
        dto.setBuyPriceBoundaryValue(3.8f);
        dto.setEmail("test@example.com");
        dto.setUserFirstName("Anna");
        dto.setUserLastName("Nowak");
        dto.setAddress("Testowa 1");
        dto.setCountry("Poland");
        dto.setPhoneNumber("123456789");
        if (!isUserNew) dto.setUserId(userId);
        return dto;
    }
}