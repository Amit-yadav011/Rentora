package com.example.rentora.repository;

import com.example.rentora.entity.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {


    //we will not need a method in when using Jpa //this is where it feels like magic
    List<Property> findByLocation(String location);


    @Query("SELECT p FROM Property p WHERE " +
            "(:location IS NULL OR p.location = :location) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Property> searchProperties(String location, Double minPrice, Double maxPrice, Pageable pageable);
}
