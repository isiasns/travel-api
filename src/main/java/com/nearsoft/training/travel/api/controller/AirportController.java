package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.AirportService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airports")
public class AirportController {
    private AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @GetMapping("/autocomplete/{term}")
    public ResponseEntity<List<Airport>> getAutocompleteAirports(@PathVariable String term) {
        if (Strings.isEmpty(term)) {
            throw new RequiredParametersException("Term is required");
        }
        List<Airport> airports = airportService.getAutocompleteAirports(term);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }
}
