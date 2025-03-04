package com.ratesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RatesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatesServiceApplication.class, args);
    }

}
