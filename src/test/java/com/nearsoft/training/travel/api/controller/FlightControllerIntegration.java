package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Flight;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FlightControllerIntegration {
    private static final String host = "http://localhost:";
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private CacheManager cacheManager;

    @Test
    public void givenOneWaySearchWhenOneWayThenReturnFlights() {
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

    @Test
    public void givenRoundTripSearchWhenRoundTripThenReturnFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String params = "origin/LAX/destination/BOS/departure/2019-01-31/return/2019-02-28";
        ResponseEntity<Map<String, List<Flight>>> response = restTemplate.exchange(createUrlWithPort("/flights/round-trip/" + params), HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, List<Flight>>>() {
        });
        Flight departureFlight1 = null;
        Flight departureFlight2 = null;
        Flight returnFlight1 = null;
        Flight returnFlight2 = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            departureFlight1 = Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build();
            departureFlight2 = Flight.builder().origin("LAX").originTerminal("5").destination("BOS").destinationTerminal("C").departureDate(dateFormat.parse("2019-01-31T15:26")).arrivalDate(dateFormat.parse("2019-01-31T23:47")).number("688").airline("B6").build();
            returnFlight1 = Flight.builder().origin("BOS").originTerminal("B").destination("LAX").destinationTerminal("7").departureDate(dateFormat.parse("2019-02-28T06:35")).arrivalDate(dateFormat.parse("2019-02-28T10:16")).number("1820").airline("UA").build();
            returnFlight2 = Flight.builder().origin("BOS").originTerminal("A").destination("LAX").destinationTerminal("2").departureDate(dateFormat.parse("2019-02-28T17:40")).arrivalDate(dateFormat.parse("2019-02-28T21:10")).number("1654").airline("DL").build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(response.getBody(), hasEntry(is("departure"), hasItems(departureFlight1, departureFlight2)));
        assertThat(response.getBody(), hasEntry(is("return"), hasItems(returnFlight1, returnFlight2)));
    }

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }

    @Test
    public void givenOneWaySearchWhenOneWayThenCachedFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String origin = "LAX";
        String destination = "BOS";
        String departureDate = "2019-01-31";
        String params = "origin/" + origin + "/destination/" + destination + "/departure/" + departureDate;
        Cache cache = cacheManager.getCache("one-way-flights");
        ResponseEntity<List<Flight>> flights = restTemplate.exchange(createUrlWithPort("/flights/one-way/" + params), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Flight>>() {
        });
        assertThat(flights.getBody().hashCode(), equalTo(cache.get(origin + destination + departureDate).get().hashCode()));
    }

    @Test
    public void givenRoundTripSearchReturnWhenRoundTripThenCachedFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String origin = "LAX";
        String destination = "BOS";
        String departureDate = "2019-01-31";
        String returnDate = "2019-02-28";
        String params = "origin/" + origin + "/destination/" + destination + "/departure/" + departureDate + "/return/" + returnDate;
        Cache cache = cacheManager.getCache("round-trip-flights");
        ResponseEntity<Map<String, List<Flight>>> flights = restTemplate.exchange(createUrlWithPort("/flights/round-trip/" + params), HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, List<Flight>>>() {
        });
        assertThat(flights.getBody().hashCode(), equalTo(cache.get(origin + destination + departureDate + returnDate).get().hashCode()));
    }

    @Test
    public void givenEmptyOneWaySearchWhenOneWayThenReturnFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort("/flights/one-way/"), HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    public void givenEmptyRoundTripSearchWhenRoundTripThenReturnFlights() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort("/flights/round-trip/"), HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.NOT_FOUND));
    }
}
