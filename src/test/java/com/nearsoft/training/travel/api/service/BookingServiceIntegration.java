package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntegration {
    @Autowired
    private BookingService bookingService;

    @Test
    public void givenTempBookingWhenSaveTempBookingThenSaveAndReturnTempBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking tempBooking = Booking.builder().user(user).departing(departing).returning(returning).build();
        Booking savedBooking = bookingService.saveTempBooking(tempBooking);
        assertThat(savedBooking.getId(), is(not(nullValue())));
        assertThat(savedBooking.getStatus(), equalTo(Booking.Status.TEMP.toString()));
    }

    @Test
    public void givenBookingWhenSaveBookingThenSaveAndReturnBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking booking = Booking.builder().user(user).departing(departing).returning(returning).build();
        Booking savedBooking = bookingService.saveBooking(booking);
        assertThat(savedBooking.getId(), is(not(nullValue())));
        assertThat(savedBooking.getStatus(), equalTo(Booking.Status.PAID.toString()));
    }

    @Test
    public void givenBookingIdWhenDeleteBookingThenDeleteBooking() {
        User user = User.builder().username("isiasns").build();
        Flight departing = Flight.builder().build();
        Flight returning = Flight.builder().build();
        Booking booking = Booking.builder().user(user).departing(departing).returning(returning).build();
        Booking savedBooking = bookingService.saveBooking(booking);
        bookingService.deleteBooking(savedBooking.getId());
    }
}
