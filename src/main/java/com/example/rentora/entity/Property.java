package com.example.rentora.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Property {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;
    private String description;
    @NotBlank
    private String location;
    @NotNull
    private Double price;

    @NotNull
    private String propertyType; //1bhk/2bhk....

    @NotNull
    private Boolean available;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable = true)
    private User owner;
}
