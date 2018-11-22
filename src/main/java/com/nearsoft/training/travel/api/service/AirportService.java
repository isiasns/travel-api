package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AirportService {
    @Autowired
    private RestTemplate restTemplate;

    public List<Airport> getAutocompleteAirports(String term) {
        if (Strings.isEmpty(term)) {
            throw new RequiredParametersException("Term is required");
        }
        String response = restTemplate.getForObject("https://api.sandbox.amadeus.com/v1.2/airports/autocomplete?apikey=Cb8yAiL9vWb6x91dPUfnwjN4I7CmjUyY&term=" + term, String.class);
        Airport[] airports = {};
        try {
            airports = new ObjectMapper().readValue(response, Airport[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(airports);
    }
}
