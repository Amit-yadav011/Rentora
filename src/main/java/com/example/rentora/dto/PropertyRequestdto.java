package com.example.rentora.dto;


import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyRequestdto {


    private String title;
    private String location;
    private String city;
    private double price;
    private String description;
    private String propertyType;
    private Boolean availability;
    private int beds;
    private int baths;
    private int sqft;
    private List<String> neighborhood;


}
