package com.example.rentora.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponsedto {

        private Long id;
        private String title;
        private String location;
        private double price;
        private String description;
        private String propertyType;
        private Boolean Availability;
}
