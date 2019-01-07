package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.repository.BookingRepository;

public class BookingService {

    private BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking saveTempBooking(Booking tempBooking) {
        return saveBooking(tempBooking, Booking.Status.TEMP);
    }

    public Booking saveBooking(Booking booking) {
        return saveBooking(booking, Booking.Status.PAID);
    }

    private Booking saveBooking(Booking booking, Booking.Status status) {
        booking.setStatus(status.toString());
        return bookingRepository.save(booking);
    }
}
