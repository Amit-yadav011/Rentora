package com.example.rentora.controller;

import com.example.rentora.entity.Reviews;
import com.example.rentora.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public Map<String,Object> response(@RequestParam Long propertyId){
        return reviewService.getAllReviews(propertyId);
    }

    @PostMapping
    public String postReview(@RequestParam Long propertyId,@RequestParam int rating, @RequestParam String comment ){
//        System.out.println("Controller Hittttt"); --this was for debugging purpose!
        return reviewService.addReview(propertyId,rating,comment);
    }
}
