package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.config.TravelApiConfig;
import com.nearsoft.training.travel.api.dao.Airport;
import com.nearsoft.training.travel.api.exception.JsonConversionException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class AirportService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TravelApiConfig travelApiConfig;


    @Cacheable(value = "autocomplete-airports", key = "#term")
    public List<Airport> getAutocompleteAirports(String term) {
        if (Strings.isEmpty(term)) {
            throw new RequiredParametersException("Term is required");
        }
        final String url = travelApiConfig.getAirportsAutocomplete() + "&term=" + term;
        final String response = restTemplate.getForObject(url, String.class);
        Airport[] airports = {};
        try {
            airports = new ObjectMapper().readValue(response, Airport[].class);
        } catch (IOException e) {
            throw new JsonConversionException(e.getMessage());
        }
        return Arrays.asList(airports);
    }
}
