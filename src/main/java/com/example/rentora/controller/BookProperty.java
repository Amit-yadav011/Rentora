package com.example.rentora.controller;



import com.example.rentora.entity.Booking;
import com.example.rentora.entity.Property;
import com.example.rentora.entity.User;
import com.example.rentora.enums.BookingStatus;
import com.example.rentora.repository.BookingRepository;
import com.example.rentora.repository.PropertyRepository;
import com.example.rentora.service.BookService;
import com.example.rentora.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking")
public class BookProperty {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/{propertyId}")
    public ResponseEntity<?> booking(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookService.addBooking(propertyId));
    }

    //get all booking of a particular tenant user not the owner!
    @GetMapping("/bookings")
    public ResponseEntity<?> getBook() {
        return ResponseEntity.ok(bookService.getAllBooking());
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<?> getProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok(bookService.getBookingOfProperty(propertyId));
    }

    //for accepting that booking
    @PutMapping("/accept/{bookingId}")
    public ResponseEntity<?> acceptBooking(@PathVariable Long bookingId) {

        User currentUser= userService.getCurrentUser();

        Booking booking=bookingRepository.findById(bookingId).orElseThrow(()-> new RuntimeException("Booking not found with that id"));

        Property prop=propertyRepository.findById(booking.getProperty().getId()).orElseThrow(()-> new RuntimeException("Property not found with that id"));

        if(!currentUser.getId().equals(prop.getOwner().getId())){
            throw new RuntimeException("You are not the owner of the property");
        }

        booking.setBookingStatus(BookingStatus.Accepted);
        bookingRepository.save(booking);

        //this is to prevent multiple acceptance of the same property
        List<Booking> otherBookings=bookingRepository.findByPropertyIdNot(booking.getProperty(),booking.getId());

        for(Booking b:otherBookings){
            if(b.getBookingStatus().equals(BookingStatus.Pending)){
                b.setBookingStatus(BookingStatus.Rejected);
            }
        }

        bookingRepository.saveAll(otherBookings);

        return ResponseEntity.ok("Booking Accepted");
    }

    @PutMapping("/reject/{bookingId}")
    public ResponseEntity<?> rejectBooking(@PathVariable Long bookingId) {

        User currentUser=userService.getCurrentUser();

        Booking booking= bookingRepository.findById(bookingId).orElseThrow(()-> new RuntimeException("No booking found with that id"));

        Property prop= booking.getProperty();

        //check for property ownership
        if(!currentUser.getId().equals(prop.getOwner().getId())){
            throw new RuntimeException("Only owner's can accept/reject bookings of their property");
        }

        booking.setBookingStatus(BookingStatus.Rejected);
        bookingRepository.save(booking);

        return ResponseEntity.ok("Booking Rejected");
    }
}
