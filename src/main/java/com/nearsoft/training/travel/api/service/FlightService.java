package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.config.TravelApiConfig;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.JsonConversionException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.util.JsonFlightsUtil;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class FlightService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TravelApiConfig travelApiConfig;

    @Cacheable(value = "one-way-flights", key = "#origin + #destination + #departureDate")
    public List<Flight> getFlights(String origin, String destination, String departureDate) {
        if (Strings.isEmpty(origin) || Strings.isEmpty(destination) || Strings.isEmpty(departureDate)) {
            throw new RequiredParametersException("Origin, destination and departure date are required");
        }
        final String url = travelApiConfig.getOneWaySearch() + "&origin=" + origin + "&destination=" + destination + "&departure_date=" + departureDate;
        final String response = restTemplate.getForObject(url, String.class);
        JsonNode rootNode;
        try {
            rootNode = new ObjectMapper().readTree(response);
        } catch (IOException e) {
            throw new JsonConversionException(e.getMessage());
        }
        return JsonFlightsUtil.getDepartureFlightsFromRootNode(rootNode);

    }

    @Cacheable(value = "round-trip-flights", key = "#origin + #destination + #departureDate + #returnDate")
    public Map<String, List<Flight>> getFlights(String origin, String destination, String departureDate, String returnDate) {
        if (Strings.isEmpty(origin) || Strings.isEmpty(destination) || Strings.isEmpty(departureDate) || Strings.isEmpty(returnDate)) {
            throw new RequiredParametersException("Origin, destination, departure date and return date are required");
        }
        final String url = travelApiConfig.getRoundTripSearch() + "&origin=" + origin + "&destination=" + destination + "&departure_date=" + departureDate + "&return_date=" + returnDate;
        final String response = restTemplate.getForObject(url, String.class);
        JsonNode rootNode;
        try {
            rootNode = new ObjectMapper().readTree(response);
        } catch (IOException e) {
            throw new JsonConversionException(e.getMessage());
        }
        return JsonFlightsUtil.getRoundTripFlightsFromRootNode(rootNode);
    }
}
