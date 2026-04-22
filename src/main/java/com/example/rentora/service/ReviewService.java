package com.example.rentora.service;

import com.example.rentora.entity.Property;
import com.example.rentora.entity.Reviews;
import com.example.rentora.entity.User;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.PropertyRepository;
import com.example.rentora.repository.ReviewRepository;
import com.example.rentora.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public String addReview(Long propertyId, int rating, String comment) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            //check auth if its valid
            if (auth == null || !auth.isAuthenticated()) {
                return "user not authenticated";
            }

            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));

            Property property = propertyRepository.findById(propertyId).orElseThrow(() -> new ResourceNotFoundException("Property find with that id"));

            if (reviewRepository.findByUserAndProperty(user, property).isPresent()) {
                return "Review already exists";
            }
            //review must be b/w 1-5
            if (rating < 1 || rating > 5) {
                return "Invalid rating";
            }

            Reviews reviews = new Reviews();
            reviews.setUser(user);
            reviews.setComment(comment);
            reviews.setRating(rating);
            reviews.setProperty(property);

            reviewRepository.save(reviews);

            return "Review added";
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Map<String,Object> getAllReviews( Long propertyId)
    {
       Property property=propertyRepository.findById(propertyId).orElseThrow(()-> new ResourceNotFoundException("Property find with that id"));

       List<Reviews> reviews=reviewRepository.findByProperty(property);

     //to show average rating of the specific property!
        double avgRating = reviews.stream()
                .mapToInt(Reviews::getRating)
                .average()
                .orElse(0.0);

       Map<String,Object> map=new HashMap<>();

       map.put("ratings",avgRating);
       map.put("reviews",reviews);

       return map;
    }
}
