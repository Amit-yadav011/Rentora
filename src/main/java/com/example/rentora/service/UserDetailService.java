package com.example.rentora.service;

import com.example.rentora.config.CustomUserDetails;
import com.example.rentora.entity.User;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
