package com.example.rentora.service;

import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.entity.Wishlist;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.PropertyRepository;
import com.example.rentora.repository.UserRepository;
import com.example.rentora.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class WishListService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishListRepository wishListRepository;

    public String addWishList( Long PropertyId){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(auth==null || auth.getName().equals("anonymousUser")|| !auth.isAuthenticated()){
            return(null);
        }
        String email=auth.getName();
        User user= userRepository.findByEmail(email).orElseThrow();

        Property property= propertyRepository.findById(PropertyId).orElseThrow();

        if(wishListRepository.findByUserAndProperty(user, property).isPresent()){
                 return("Already added in WishList");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProperty(property);

        wishListRepository.save(wishlist);
        return "Added to WishList";

    }


    public List<Property> findByUser() {

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();

        if(auth==null || auth.getName().equals("anonymousUser")|| !auth.isAuthenticated()){
            return(null);
        }
        String email=auth.getName();
        User user= userRepository.findByEmail(email).orElseThrow();

        return wishListRepository.findByUser(user)
                .stream().map(Wishlist::getProperty)
                .toList();
    }

    public String deleteUserAndProperty(Long PropertyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return("Not logged in");
        }
        String email=authentication.getName();
        User user= userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found"));

        Property property= propertyRepository.findById(PropertyId).orElseThrow(()-> new ResourceNotFoundException("Property not found"));

        wishListRepository.deleteByUserAndProperty(user, property);

        return "Deleted from WishList";
    }
}
