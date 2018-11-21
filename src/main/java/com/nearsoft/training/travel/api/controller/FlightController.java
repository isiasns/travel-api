package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.FlightService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public class FlightController {
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    private FlightService flightService;

    public ResponseEntity<List<Flight>> getOneWayFlights(String origin, String destination, String departureDate) {
        if (Strings.isEmpty(origin) || Strings.isEmpty(destination) || Strings.isEmpty(departureDate)) {
            throw new RequiredParametersException("Origin, destination and departure date are required");
        }
        List<Flight> flights = flightService.getFlights(origin, destination, departureDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    public ResponseEntity<Map<String, List<Flight>>> getRoundTripFlights(String origin, String destination, String departureDate, String returnDate) {
        if (Strings.isEmpty(origin) || Strings.isEmpty(destination) || Strings.isEmpty(departureDate) || Strings.isEmpty(returnDate)) {
            throw new RequiredParametersException("Origin, destination, departure date and return date are required");
        }
        Map<String, List<Flight>> flights = flightService.getFlights(origin, destination, departureDate, returnDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }
}
