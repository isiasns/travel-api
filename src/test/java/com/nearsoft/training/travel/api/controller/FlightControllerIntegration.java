package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Flight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegration {
    private static final String host = "http://localhost:";
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void givenOneWaySearchWhenGetFlightsThenReturnFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String params = "origin/LAX/destination/BOS/departure/2019-01-31";
        ResponseEntity<List<Flight>> response = restTemplate.exchange(createUrlWithPort("/flights/one-way/" + params), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Flight>>() {
        });
        Flight flight1 = null;
        Flight flight2 = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            flight1 = Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build();
            flight2 = Flight.builder().origin("LAX").originTerminal("5").destination("BOS").destinationTerminal("C").departureDate(dateFormat.parse("2019-01-31T15:26")).arrivalDate(dateFormat.parse("2019-01-31T23:47")).number("688").airline("B6").build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(response.getBody(), hasItems(flight1, flight2));
    }

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }
}
