package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.AirportService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class AirportController {
    private AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    public ResponseEntity<List<Airport>> getAutocompleteAirports(String term) {
        if (Strings.isEmpty(term)) {
            throw new RequiredParametersException("Term is required");
        }
        List<Airport> airports = airportService.getAutocompleteAirports(term);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }
}
