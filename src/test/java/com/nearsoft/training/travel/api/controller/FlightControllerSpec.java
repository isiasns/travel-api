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
import java.util.List;

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
    public void givenOneWaySearchWhenGetFlightsThenReturnFlights() throws Exception {
        List<Flight> flights = new ArrayList<>();
        flights.add(Flight.builder().build());
        expect(flightService.getOneWayFlights(anyString(), anyString(), anyString())).andReturn(flights);
        replay(flightService);
        ResponseEntity<List<Flight>> result = flightController.getOneWayFlights("HMO", "CUU", "2018-12-31");
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().size(), equalTo(flights.size()));
        assertThat(result.getBody().get(0).getOrigin(), equalTo(flights.get(0).getOrigin()));
        assertThat(result.getBody().get(0).getDestination(), equalTo(flights.get(0).getDestination()));
        assertThat(result.getBody().get(0).getDepartureDate(), equalTo(flights.get(0).getDepartureDate()));
        verify(flightService);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenBadOneWaySearchWhenGetFlightsThenThrowException() {
        flightController.getOneWayFlights(null, null, null);
    }
}
