package com.example.subscriptionservice.service;

import com.example.subscriptionapi.dto.UserDto;
import com.example.subscriptionservice.model.UserMapper;
import com.example.subscriptionservice.model.entities.AppUser;
import com.example.subscriptionservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserApi {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void addUser(UserDto userDto) {
        userRepository.save(userMapper.toEntity(userDto));
    }

    public List<AppUser> getAllSubscriptions() {
        return userRepository.findAll();
    }
}
