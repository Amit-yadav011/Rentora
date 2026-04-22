package com.example.rentora.dto;


import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequestdto {


    private String title;
    private String location;
    private double price;
    private String description;
    private String propertyType;
    private Boolean Availability;

}
