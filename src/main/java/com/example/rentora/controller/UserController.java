package com.example.rentora.controller;



import com.example.rentora.entity.User;
import com.example.rentora.service.UserDetailService;
import com.example.rentora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

 @GetMapping("/me")
    public User getUser(){
     return userService.getCurrentUser();
 }
}
