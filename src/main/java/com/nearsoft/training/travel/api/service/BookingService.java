package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.exception.NoBookingFoundException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
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
        if (booking == null || status == null) {
            throw new RequiredParametersException("Booking and status are required");
        }
        booking.setStatus(status.toString());
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long bookingId) {
        if (bookingId == null) {
            throw new RequiredParametersException("Booking id is required");
        }
        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional == null) {
            throw new NoBookingFoundException("Booking does not exists!");
        }
        bookingRepository.delete(optional.get());
    }
}
