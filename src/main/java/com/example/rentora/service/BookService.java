package com.example.rentora.service;

import com.example.rentora.entity.Booking;
import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.enums.BookingStatus;
import com.example.rentora.enums.Role;
import com.example.rentora.exception.ResourceNotFoundException;
import com.example.rentora.repository.BookingRepository;
import com.example.rentora.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserService userService;

    public String addBooking(Long propertyId){

        User currentUser=userService.getCurrentUser();

        Property property=propertyRepository.findById(propertyId).orElseThrow(()->new ResourceNotFoundException("Property not found"));

        if(!currentUser.getId().equals(property.getOwner().getId())){
            throw new ResourceNotFoundException("Owner can not book their properties");
        }

        //to avoid multiple booking of same property
        Optional<Booking> existing=bookingRepository.findByUserAndProperty(currentUser,property);
        if(existing.isPresent() && existing.get().getBookingStatus().equals(BookingStatus.Pending) ||
                existing.get().getBookingStatus().equals(BookingStatus.Accepted)
        ){
            throw new RuntimeException("Booking already exists");
        }

        Booking booking=new Booking();
        booking.setUser(currentUser);
        booking.setProperty(property);
        booking.setBookingStatus(BookingStatus.Pending);
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepository.save(booking);

        return "Property Booked";
    }

    //get all booking of particular user
    public ResponseEntity<?> getAllBooking(){

        User currentUser=userService.getCurrentUser();

        if(currentUser.getRole().equals(Role.LANDLORD)){
            throw new RuntimeException("Only Tenants can access their bookings");
        }

        return ResponseEntity.ok(bookingRepository.findByUser(currentUser));
    }

    //this is to check owner's all property's booking
    public ResponseEntity<?> getBookingOfProperty(Long propertyId){

        User currentUser=userService.getCurrentUser();

        Property property=propertyRepository.findById(propertyId).orElseThrow(()->new ResourceNotFoundException("Property not found"));

        if(!currentUser.getId().equals(property.getOwner().getId())){
            throw new ResourceNotFoundException("you are not owner of the property");
        }

        return ResponseEntity.ok(bookingRepository.findByProperty(property));
    }
}
