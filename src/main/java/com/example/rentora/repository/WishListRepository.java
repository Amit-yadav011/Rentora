package com.example.rentora.repository;


import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProperty(User user,  Property property);

    void deleteByUserAndProperty(User user,  Property property);
}
