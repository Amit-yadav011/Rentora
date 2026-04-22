package com.example.rentora.repository;

import com.example.rentora.entity.Booking;
import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {


    Optional<Booking> findByUserAndProperty(User user, Property property);

    List<Booking> findByUser(User user);

    List<Booking> findByProperty(Property property);

    List<Booking> findByPropertyIdNot(Property property,Long id);
}
