package com.example.rentora.service;

import com.example.rentora.entity.User;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User getCurrentUser(){

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new ResourceNotFoundException("Authentication required");
        }

        String email= authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
    }
}
