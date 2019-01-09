package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/temp")
    public ResponseEntity<Booking> saveTempBooking(@RequestBody Booking tempBooking) {
        if (tempBooking == null) {
            throw new RequiredParametersException("Booking is required");
        }
        return new ResponseEntity<>(bookingService.saveTempBooking(tempBooking), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Booking> saveBooking(@RequestBody Booking booking) {
        if (booking == null) {
            throw new RequiredParametersException("Booking is required");
        }
        return new ResponseEntity<>(bookingService.saveBooking(booking), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity deleteBooking(@PathVariable Long bookingId) {
        if (bookingId == null) {
            throw new RequiredParametersException("Booking id is required");
        }
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
