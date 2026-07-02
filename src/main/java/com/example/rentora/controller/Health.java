package com.example.rentora.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Health {

    @GetMapping("/health12")
    public String health(){
        return "health";
    }
}
