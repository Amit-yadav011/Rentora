package com.example.rentora.repository;

import com.example.rentora.entity.Property;
import com.example.rentora.entity.Reviews;
import com.example.rentora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Reviews, Long> {

    List<Reviews> findByProperty(Property property);

    Optional<Reviews> findByUserAndProperty(User user, Property property);
}
