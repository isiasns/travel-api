package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.config.TravelApiConfig;
import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.JsonConversionException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(EasyMockRunner.class)
public class AirportServiceSpec {
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private TravelApiConfig travelApiConfig;
    @TestSubject
    private AirportService airportService = new AirportService();

    @Test
    public void givenTermWhenGetAutocompleteAirportsThenReturnAirports() {
        List<Airport> airportList = new ArrayList<>();
        airportList.add(Airport.builder().iataCode("BOS").name("Boston - Logan International Airport [BOS]").build());
        airportList.add(Airport.builder().iataCode("PSM").name("Boston - Portsmouth International Airport at Pease [PSM]").build());
        airportList.add(Airport.builder().iataCode("BST").name("Lashkar Gāh - Bost Airfield [BST]").build());
        String airportsString = null;
        try {
            airportsString = new ObjectMapper().writeValueAsString(airportList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        expect(travelApiConfig.getAirportsAutocomplete()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn(airportsString);
        replay(travelApiConfig, restTemplate);
        List<Airport> airports = airportService.getAutocompleteAirports("Bost");
        verify(travelApiConfig, restTemplate);
        assertThat(airports.size(), equalTo(3));
        assertThat(airports.get(0).getIataCode(), equalTo(airportList.get(0).getIataCode()));
        assertThat(airports.get(1).getIataCode(), equalTo(airportList.get(1).getIataCode()));
        assertThat(airports.get(2).getIataCode(), equalTo(airportList.get(2).getIataCode()));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyTermWhenGetAutocompleteAirportsThenThrowException() {
        airportService.getAutocompleteAirports(null);
    }

    @Test(expected = RestClientException.class)
    public void givenTermWhenGetAutocompleteAirportsAndBadRequestThenThrowException() {
        expect(travelApiConfig.getAirportsAutocomplete()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andThrow(new RestClientException(""));
        replay(travelApiConfig, restTemplate);
        airportService.getAutocompleteAirports("Bost");
        verify(travelApiConfig, restTemplate);
    }

    @Test(expected = JsonConversionException.class)
    public void givenTermWhenGetAutocompleteAirportsAndBadContentThenThrowException() {
        expect(travelApiConfig.getAirportsAutocomplete()).andReturn("");
        expect(restTemplate.getForObject(anyString(), anyObject())).andReturn("error");
        replay(travelApiConfig, restTemplate);
        airportService.getAutocompleteAirports("Bost");
        verify(travelApiConfig, restTemplate);
    }
}
