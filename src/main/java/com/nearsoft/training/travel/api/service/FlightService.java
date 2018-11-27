package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nearsoft.training.travel.api.config.TravelApiConfig;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class FlightService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private TravelApiConfig travelApiConfig;
    @Autowired
    private DateFormat dateFormat;

    public List<Flight> getFlights(String origin, String destination, String departureDate) {
        if (Strings.isEmpty(origin) || Strings.isEmpty(destination) || Strings.isEmpty(departureDate)) {
            throw new RequiredParametersException("Origin, destination and departure date are required");
        }
        final String url = travelApiConfig.getOneWaySearch() + "&origin=" + origin + "&destination=" + destination + "&departure_date=" + departureDate;
        final String response = restTemplate.getForObject(url, String.class);
        JsonNode rootNode = null;
        try {
            rootNode = new ObjectMapper().readTree(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getOutboundFlightsFromRootNode(rootNode);

    }

    private List<Flight> getOutboundFlightsFromRootNode(JsonNode rootNode) {
        List<Flight> flights = new ArrayList<>();
        Iterator<JsonNode> resultsNode = rootNode.get("results").elements();
        while (resultsNode.hasNext()) {
            JsonNode resultNode = resultsNode.next();
            if (resultNode.has("itineraries")) {
                Iterator<JsonNode> itinerariesNode = resultNode.get("itineraries").elements();
                while (itinerariesNode.hasNext()) {
                    JsonNode itineraryNode = itinerariesNode.next();
                    Iterator<JsonNode> flightsNode = itineraryNode.get("outbound").get("flights").elements();
                    while (flightsNode.hasNext()) {
                        JsonNode flightNode = flightsNode.next();
                        flights.add(getFlightFromNode(flightNode));
                    }
                }
            }
        }
        return flights;
    }

    private Flight getFlightFromNode(JsonNode flightNode) {
        Flight.FlightBuilder flightBuilder = Flight.builder();
        Iterator<Map.Entry<String, JsonNode>> fieldsNode = flightNode.fields();
        while (fieldsNode.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsNode.next();
            String key = field.getKey();
            JsonNode value = field.getValue();
            switch (key) {
                case "departs_at":
                case "arrives_at":
                    try {
                        if (field.getKey().equals("departs_at")) {
                            flightBuilder.departureDate(dateFormat.parse(value.asText()));
                        } else {
                            flightBuilder.arrivalDate(dateFormat.parse(value.asText()));
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case "origin":
                    flightBuilder.origin(value.get("airport").asText());
                    if (value.has("terminal")) {
                        flightBuilder.originTerminal(value.get("terminal").asText());
                    }
                    break;
                case "destination":
                    flightBuilder.destination(value.get("airport").asText());
                    if (value.has("terminal")) {
                        flightBuilder.destinationTerminal(value.get("terminal").asText());
                    }
                    break;
                case "flight_number":
                    flightBuilder.number(value.asText());
                    break;
                case "operating_airline":
                    flightBuilder.airline(value.asText());
                    break;
                default:
                    break;
            }
        }
        return flightBuilder.build();
    }

    public Map<String, List<Flight>> getFlights(String origin, String destination, String departureDate, String returningDate) {
        return null;
    }
}
