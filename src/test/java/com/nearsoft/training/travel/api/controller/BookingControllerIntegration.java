package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerIntegration {
    private static final String host = "http://localhost:";
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }

    @Test
    public void givenTempBookingWhenBookingsTempThenReturnTempBookingAndStatus() {
        User user = User.builder().username("isiasns").build();
        Booking tempBooking = Booking.builder().user(user).departing(Flight.builder().build()).returning(Flight.builder().build()).build();
        HttpEntity<Booking> entity = new HttpEntity<>(tempBooking, headers);
        ResponseEntity<Booking> response = restTemplate.exchange(createUrlWithPort("/bookings/temp/"), HttpMethod.POST, entity, new ParameterizedTypeReference<Booking>() {
        });
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getStatus(), equalTo(Booking.Status.TEMP.toString()));
    }

    @Test
    public void givenBookingWhenBookingsAddThenReturnBookingAndStatusOk() {
        User user = User.builder().username("isiasns").build();
        Booking tempBooking = Booking.builder().user(user).departing(Flight.builder().build()).returning(Flight.builder().build()).build();
        HttpEntity<Booking> entity = new HttpEntity<>(tempBooking, headers);
        ResponseEntity<Booking> response = restTemplate.exchange(createUrlWithPort("/bookings/add/"), HttpMethod.POST, entity, new ParameterizedTypeReference<Booking>() {
        });
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody().getStatus(), equalTo(Booking.Status.PAID.toString()));
    }

    @Test
    public void givenBookingIdWhenBookingsDeleteThenStatusNoContent() {
        User user = User.builder().username("isiasns").build();
        Booking tempBooking = Booking.builder().user(user).departing(Flight.builder().build()).returning(Flight.builder().build()).build();
        HttpEntity<Booking> postEntity = new HttpEntity<>(tempBooking, headers);
        ResponseEntity<Booking> postResponse = restTemplate.exchange(createUrlWithPort("/bookings/add/"), HttpMethod.POST, postEntity, new ParameterizedTypeReference<Booking>() {
        });
        HttpEntity entity = new HttpEntity<>(null, headers);
        ResponseEntity response = restTemplate.exchange(createUrlWithPort("/bookings/delete/" + postResponse.getBody().getId()), HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NO_CONTENT));
    }
}
