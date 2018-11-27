package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Flight;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
public class FlightServiceIntegration {
    @Autowired
    private FlightService flightService;

    @Test
    public void givenOriginDestinationDepartureWhenGetFlightsThenReturnFlights() {
        List<Flight> flights = flightService.getFlights("LAX", "BOS", "2019-01-31");
        assertThat(flights, is(not(empty())));
    }
}
