package com.nearsoft.training.travel.api.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.nearsoft.training.travel.api.dao.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JsonFlightsUtil {
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");;

    public static List<Flight> getDepartureFlightsFromRootNode(JsonNode rootNode) {
        return getFlightsFromRootNode(rootNode, "outbound");
    }

    private static List<Flight> getReturnFlightsFromRootNode(JsonNode rootNode) {
        return getFlightsFromRootNode(rootNode, "inbound");
    }

    private static List<Flight> getFlightsFromRootNode(JsonNode rootNode, String nodeName) {
        List<Flight> flights = new ArrayList<Flight>();
        Iterator<JsonNode> resultsNode = rootNode.get("results").elements();
        while (resultsNode.hasNext()) {
            JsonNode resultNode = resultsNode.next();
            if (resultNode.has("itineraries")) {
                Iterator<JsonNode> itinerariesNode = resultNode.get("itineraries").elements();
                while (itinerariesNode.hasNext()) {
                    JsonNode itineraryNode = itinerariesNode.next();
                    Iterator<JsonNode> flightsNode = itineraryNode.get(nodeName).get("flights").elements();
                    while (flightsNode.hasNext()) {
                        JsonNode flightNode = flightsNode.next();
                        flights.add(getFlightFromNode(flightNode));
                    }
                }
            }
        }
        return flights;
    }

    private static Flight getFlightFromNode(JsonNode flightNode) {
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

    public static Map<String, List<Flight>> getRoundTripFlightsFromRootNode(JsonNode rootNode) {
        Map<String, List<Flight>> flights = new HashMap<>();
        flights.put("departure", getDepartureFlightsFromRootNode(rootNode));
        flights.put("return", getReturnFlightsFromRootNode(rootNode));
        return flights;
    }
}