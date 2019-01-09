package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.BookingService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(EasyMockRunner.class)
public class BookingControllerSpec {
    @Mock
    private BookingService bookingService;
    @TestSubject
    private BookingController bookingController = new BookingController(bookingService);

    @Test
    public void givenTempBookingWhenSaveTempBookingThenSaveAndReturnBooking() {
        Booking booking = Booking.builder().build();
        booking.setStatus(Booking.Status.TEMP.toString());
        expect(bookingService.saveTempBooking(anyObject())).andReturn(booking);
        replay(bookingService);
        ResponseEntity<Booking> result = bookingController.saveTempBooking(booking);
        verify(bookingService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().getStatus(), equalTo(Booking.Status.TEMP.toString()));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyBookingWhenSaveTempBookingThenThrowException() {
        bookingController.saveTempBooking(null);
    }

    @Test
    public void givenTempBookingWhenSaveBookingThenSaveAndReturnBooking() {
        Booking booking = Booking.builder().build();
        booking.setStatus(Booking.Status.PAID.toString());
        expect(bookingService.saveTempBooking(anyObject())).andReturn(booking);
        replay(bookingService);
        ResponseEntity<Booking> result = bookingController.saveBooking(booking);
        verify(bookingService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().getStatus(), equalTo(Booking.Status.PAID.toString()));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyBookingWhenSaveBookingThenThrowException() {
        bookingController.saveBooking(null);
    }

    @Test
    public void givenBookingIdWhenDeleteBookingThenNoContent() {
        bookingService.deleteBooking(anyLong());
        expectLastCall().once();
        replay(bookingService);
        ResponseEntity result = bookingController.deleteBooking(0L);
        verify(bookingService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyBookingIdWhenDeleteBookingThenThrow() {
        bookingController.deleteBooking(null);
    }
}
