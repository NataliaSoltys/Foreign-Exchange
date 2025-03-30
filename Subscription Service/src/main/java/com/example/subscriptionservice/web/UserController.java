package com.example.subscriptionservice.web;

import com.example.subscriptionapi.dto.UserDto;
import com.example.subscriptionservice.model.entities.User;
import com.example.subscriptionservice.service.UserApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserApi userApi;

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping
    @RequestMapping("/add")
    public ResponseEntity<Object> addUser(@RequestBody UserDto userDto) {
        try {
            userApi.addUser(userDto);
            logger.info("Created user with data: {}", userDto.toString());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @RequestMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userApi.getAllSubscriptions();
        if (users.isEmpty()) {
            logger.info("No users found");
            return ResponseEntity.noContent().build();
        }
        logger.info("Fetched {} users", users.size());
        return ResponseEntity.ok(users);
    }

}