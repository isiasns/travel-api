package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.AirportService;
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
public class AirportControllerSpec {
    @Mock
    private AirportService airportService;
    @TestSubject
    private AirportController airportController = new AirportController(airportService);

    @Test
    public void givenAutocompleteSearchWhenGetAutocompleteAirportsThenReturnAirports() {
        List<Airport> airports = new ArrayList<>();
        airports.add(Airport.builder().build());
        expect(airportService.getAutocompleteAirports(anyString())).andReturn(airports);
        replay(airportService);
        ResponseEntity<List<Airport>> result = airportController.getAutocompleteAirports("Herm");
        verify(airportService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().size(), equalTo(airports.size()));
        assertThat(result.getBody().get(0).getIataCode(), equalTo(airports.get(0).getIataCode()));
        assertThat(result.getBody().get(0).getName(), equalTo(airports.get(0).getName()));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyAutocompleteSearchWhenGetAutocompleteAirportsThenThrowException() {
        airportController.getAutocompleteAirports(null);
    }
}
