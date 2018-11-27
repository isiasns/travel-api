package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.FlightService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flights")
public class FlightController {
    private FlightService flightService;

    @Autowired
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/one-way/origin/{origin}/destination/{destination}/departure/{departureDate}")
    public ResponseEntity<List<Flight>> getOneWayFlights(@PathVariable String origin, @PathVariable String destination, @PathVariable String departureDate) {
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
