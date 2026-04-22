package com.example.rentora.controller;

import com.example.rentora.dto.LoginRequest;
import com.example.rentora.dto.UserRegisterRequest;
import com.example.rentora.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

      @Autowired
      private AuthService authService;


      @PostMapping("/register")
      public String registerUser(@RequestBody UserRegisterRequest request){
          return authService.register(request);
      }

      //for login
      @PostMapping("/login")
      public String login(@RequestBody LoginRequest request){
            return authService.Login(request);
      }
}
