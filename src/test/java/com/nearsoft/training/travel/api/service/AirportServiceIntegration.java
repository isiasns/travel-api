package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.Airport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AirportServiceIntegration {
    @Autowired
    private AirportService airportService;

    @Test
    public void givenTermWhenGetAutocompleteAirportsThenReturnAirports() {
        List<Airport> airports = airportService.getAutocompleteAirports("Bost");
        assertThat(airports, is(not(empty())));
    }
}
