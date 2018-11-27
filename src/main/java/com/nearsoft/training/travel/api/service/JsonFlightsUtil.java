package com.nearsoft.training.travel.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nearsoft.training.travel.api.dao.Flight;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonFlightsUtil {
    @Autowired
    private DateFormat dateFormat;

    public List<Flight> getOutboundFlightsFromRootNode(JsonNode rootNode) {
        List<Flight> flights = new ArrayList<Flight>();
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
}