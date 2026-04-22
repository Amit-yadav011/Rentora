package com.example.rentora.service;


import com.example.rentora.config.JwtUtil;
import com.example.rentora.dto.LoginRequest;
import com.example.rentora.dto.UserRegisterRequest;
import com.example.rentora.entity.User;
import com.example.rentora.enums.Role;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;


    public String register(UserRegisterRequest request){

        //check if email exists

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return "User already exists";
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

       //encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        //convert string to enum
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));

        userRepository.save(user);

        return "User registered successfully";
    }

    public String Login(LoginRequest request){

        User user=userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new ResourceNotFoundException("Wrong password");
        }

        return jwtUtil.generateToken(request.getEmail());
    }
}
