package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.config.TravelApiConfig;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.JsonConvetionException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@RunWith(EasyMockRunner.class)
public class FlightServiceSpec {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TravelApiConfig travelApiConfig;
    @Mock
    private JsonFlightsUtil jsonFlightsUtil;
    @TestSubject
    private FlightService flightService = new FlightService();

    @Test
    public void givenOriginDestinationDepartureWhenGetFlightsThenReturnFlights() {
        List<Flight> flightList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            flightList.add(Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build());
            flightList.add(Flight.builder().origin("LAX").originTerminal("5").destination("BOS").destinationTerminal("C").departureDate(dateFormat.parse("2019-01-31T15:26")).arrivalDate(dateFormat.parse("2019-01-31T23:47")).number("688").airline("B6").build());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String flightsString = null;
        try {
            flightsString = new ObjectMapper().writeValueAsString(flightList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        expect(travelApiConfig.getOneWaySearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn(flightsString);
        expect(jsonFlightsUtil.getDepartureFlightsFromRootNode(anyObject())).andReturn(flightList);
        replay(travelApiConfig, restTemplate, jsonFlightsUtil);
        List<Flight> flights = flightService.getFlights("LAX", "BOS", "2019-01-31");
        verify(travelApiConfig, restTemplate, jsonFlightsUtil);
        assertThat(flights.size(), greaterThanOrEqualTo(2));
        assertThat(flights.get(0).getOrigin(), equalTo("LAX"));
        assertThat(flights.get(1).getOrigin(), equalTo("LAX"));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyOriginDestinationDepartureWhenGetFlightsThenThrowException() {
        flightService.getFlights(null, null, null);
    }

    @Test(expected = RestClientException.class)
    public void givenEmptyOriginDestinationDepartureWhenGetFlightsAndBadRequestThenThrowException() {
        expect(travelApiConfig.getOneWaySearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andThrow(new RestClientException(""));
        replay(travelApiConfig, restTemplate);
        flightService.getFlights("LAX", "BOS", "2019-01-31");
        verify(travelApiConfig, restTemplate);
    }

    @Test(expected = JsonConvetionException.class)
    public void givenEmptyOriginDestinationDepartureWhenGetFlightsAndBadContentThenThrowException() {
        expect(travelApiConfig.getOneWaySearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn("error");
        replay(travelApiConfig, restTemplate);
        flightService.getFlights("LAX", "BOS", "2019-01-31");
        verify(travelApiConfig, restTemplate);
    }

    @Test
    public void givenOriginDestinationDepartureReturnWhenGetFlightsThenReturnFlights() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Map<String, List<Flight>> flightsMap = new HashMap<>();
        List<Flight> departureFlights = new ArrayList<>();
        try {
            departureFlights.add(Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build());
            departureFlights.add(Flight.builder().origin("LAX").originTerminal("5").destination("BOS").destinationTerminal("C").departureDate(dateFormat.parse("2019-01-31T15:26")).arrivalDate(dateFormat.parse("2019-01-31T23:47")).number("688").airline("B6").build());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        flightsMap.put("departure", departureFlights);
        List<Flight> returnFlights = new ArrayList<>();
        try {
            returnFlights.add(Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build());
            returnFlights.add(Flight.builder().origin("LAX").originTerminal("5").destination("BOS").destinationTerminal("C").departureDate(dateFormat.parse("2019-01-31T15:26")).arrivalDate(dateFormat.parse("2019-01-31T23:47")).number("688").airline("B6").build());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        flightsMap.put("return", returnFlights);
        String flightsString = null;
        try {
            flightsString = new ObjectMapper().writeValueAsString(flightsMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        expect(travelApiConfig.getRoundTripSearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn(flightsString);
        expect(jsonFlightsUtil.getRoundTripFlightsFromRootNode(anyObject())).andReturn(flightsMap);
        replay(travelApiConfig, restTemplate, jsonFlightsUtil);
        Map<String, List<Flight>> flights = flightService.getFlights("LAX", "BOS", "2019-01-31", "2019-02-28");
        verify(travelApiConfig, restTemplate, jsonFlightsUtil);
        assertThat(flights.size(), greaterThanOrEqualTo(2));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyOriginDestinationDepartureReturnWhenGetFlightsThenThrowException() {
        flightService.getFlights(null, null, null, null);
    }

    @Test(expected = RestClientException.class)
    public void givenEmptyOriginDestinationDepartureReturnWhenGetFlightsAndBadRequestThenThrowException() {
        expect(travelApiConfig.getRoundTripSearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andThrow(new RestClientException(""));
        replay(travelApiConfig, restTemplate);
        flightService.getFlights("LAX", "BOS", "2019-01-31", "2019-02-28");
        verify(travelApiConfig, restTemplate);
    }

    @Test(expected = JsonConvetionException.class)
    public void givenEmptyOriginDestinationDepartureReturnWhenGetFlightsAndBadContentThenThrowException() {
        expect(travelApiConfig.getRoundTripSearch()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn("error");
        replay(travelApiConfig, restTemplate);
        flightService.getFlights("LAX", "BOS", "2019-01-31", "2019-02-28");
        verify(travelApiConfig, restTemplate);
    }
}
