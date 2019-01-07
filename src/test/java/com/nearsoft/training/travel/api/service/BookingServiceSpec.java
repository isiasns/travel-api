package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.NoBookingFoundException;
import com.nearsoft.training.travel.api.repository.BookingRepository;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Optional;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(EasyMockRunner.class)
public class BookingServiceSpec {
    @Mock
    private BookingRepository bookingRepository;
    @TestSubject
    private BookingService bookingService = new BookingService(bookingRepository);

    @Test
    public void givenTempBookingWhenSaveTempBookingThenSaveAndReturnBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking tempBooking = Booking.builder().user(user).departing(departing).returning(returning).build();
        expect(bookingRepository.save(anyObject())).andReturn(tempBooking);
        replay(bookingRepository);
        Booking booking = bookingService.saveTempBooking(tempBooking);
        assertThat(booking.getStatus(), equalTo(Booking.Status.TEMP.toString()));
        verify(bookingRepository);
    }

    @Test
    public void givenBookingWhenSaveBookingThenSaveAndReturnBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking savedBooking = Booking.builder().user(user).departing(departing).returning(returning).build();
        expect(bookingRepository.save(anyObject())).andReturn(savedBooking);
        replay(bookingRepository);
        Booking booking = bookingService.saveBooking(savedBooking);
        assertThat(booking.getStatus(), equalTo(Booking.Status.PAID.toString()));
        verify(bookingRepository);
    }

    @Test
    public void givenBookingIdWhenDeleteBookingThenDeleteBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking savedBooking = Booking.builder().user(user).departing(departing).returning(returning).build();
        Optional<Booking> optional = Optional.of(savedBooking);
        expect(bookingRepository.findById(anyLong())).andReturn(optional);
        bookingRepository.delete(anyObject());
        expectLastCall().once();
        replay(bookingRepository);
        bookingService.deleteBooking(savedBooking.getId());
        verify(bookingRepository);
    }

    @Test(expected = NoBookingFoundException.class)
    public void givenWrongBookingIdWhenDeleteBookingThenThrowException() {
        expect(bookingRepository.findById(anyLong())).andReturn(null);
        replay(bookingRepository);
        bookingService.deleteBooking(0L);
        verify(bookingRepository);
    }
}
