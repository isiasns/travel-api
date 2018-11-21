package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.FlightService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(EasyMockRunner.class)
public class FlightControllerSpec {
    @Mock
    private FlightService flightService;
    @TestSubject
    private FlightController flightController = new FlightController(flightService);

    @Test
    public void givenOneWaySearchWhenGetFlightsThenReturnFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(Flight.builder().build());
        expect(flightService.getFlights(anyString(), anyString(), anyString())).andReturn(flights);
        replay(flightService);
        ResponseEntity<List<Flight>> result = flightController.getOneWayFlights("HMO", "CUU", "2018-12-31");
        verify(flightService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().size(), equalTo(flights.size()));
        assertThat(result.getBody().get(0).getOrigin(), equalTo(flights.get(0).getOrigin()));
        assertThat(result.getBody().get(0).getDestination(), equalTo(flights.get(0).getDestination()));
        assertThat(result.getBody().get(0).getDepartureDate(), equalTo(flights.get(0).getDepartureDate()));
        reset(flightService);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenBadOneWaySearchWhenGetFlightsThenThrowException() {
        flightController.getOneWayFlights(null, null, null);
    }

    @Test
    public void givenRoundTripSearchWhenGetFlightsThenReturnFlights() {
        List<Flight> departing = new ArrayList<>();
        departing.add(Flight.builder().build());
        List<Flight> returning = new ArrayList<>();
        returning.add(Flight.builder().build());
        Map<String, List<Flight>> flights = new HashMap<>();
        flights.put("departing", departing);
        flights.put("returning", returning);
        expect(flightService.getFlights(anyString(), anyString(), anyString(), anyString())).andReturn(flights);
        replay(flightService);
        ResponseEntity<Map<String, List<Flight>>> result = flightController.getRoundTripFlights("HMO", "CUU", "2018-12-31", "2019-01-31");
        verify(flightService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().size(), equalTo(flights.size()));
        assertThat(result.getBody().get("departing").size(), equalTo(flights.get("departing").size()));
        assertThat(result.getBody().get("departing").get(0).getOrigin(), equalTo(flights.get("returning").get(0).getDestination()));
        assertThat(result.getBody().get("departing").get(0).getDepartureDate(), equalTo(flights.get("departing").get(0).getDepartureDate()));
        assertThat(result.getBody().get("returning").size(), equalTo(flights.get("returning").size()));
        assertThat(result.getBody().get("returning").get(0).getOrigin(), equalTo(flights.get("departing").get(0).getDestination()));
        assertThat(result.getBody().get("returning").get(0).getDepartureDate(), equalTo(flights.get("returning").get(0).getDepartureDate()));
        reset(flightService);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenBadRoundTripSearchWhenGetFlightsThenThrowException() {
        flightController.getRoundTripFlights(null, null, null, null);
    }
}
