package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.Flight;

import java.util.ArrayList;
import java.util.List;

public class FlightService {
    public List<Flight> getOneWayFlights(String origin, String destination, String departureDate) {
        List<Flight> flights = new ArrayList<>();
        flights.add(Flight.builder().build());
        return flights;
    }
}
