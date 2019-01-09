package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BookingController {
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public ResponseEntity<Booking> saveTempBooking(Booking tempBooking) {
        if (tempBooking == null) {
            throw new RequiredParametersException("Booking is required");
        }
        return new ResponseEntity<>(bookingService.saveTempBooking(tempBooking), HttpStatus.OK);
    }

    public ResponseEntity<Booking> saveBooking(Booking booking) {
        if (booking == null) {
            throw new RequiredParametersException("Booking is required");
        }
        return new ResponseEntity<>(bookingService.saveTempBooking(booking), HttpStatus.OK);
    }

    public ResponseEntity deleteBooking(Long bookingId) {
        if (bookingId == null) {
            throw new RequiredParametersException("Booking id is required");
        }
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
