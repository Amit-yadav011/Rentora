package com.example.rentora.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reviews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String comment;


    @JoinColumn(name="user_id")
    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name="property_id")
    private Property property;
}
