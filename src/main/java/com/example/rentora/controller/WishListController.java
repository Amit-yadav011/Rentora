package com.example.rentora.controller;


import com.example.rentora.entity.Property;
import com.example.rentora.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @PostMapping("/add")
    public String addToWishList(@RequestParam Long propertyId) {
        return wishListService.addWishList( propertyId);
    }

    @GetMapping
    public List<Property> getWishlistedOnes() {
        return wishListService.findByUser();
    }

    @DeleteMapping
    public String deleteWishList( @RequestParam Long propertyId) {
        return wishListService.deleteUserAndProperty(propertyId);
    }
}
