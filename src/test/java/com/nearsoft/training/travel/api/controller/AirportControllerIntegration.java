package com.nearsoft.training.travel.api.controller;

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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AirportControllerIntegration {
    private static final String host = "http://localhost:";
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();
    @Autowired
    private CacheManager cacheManager;

    @Test
    public void givenTermWhenAirportsAutocompleteThenReturnAirports() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String term = "Bost";
        ResponseEntity<List<Airport>> response = restTemplate.exchange(createUrlWithPort("/airports/autocomplete/" + term), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Airport>>() {
        });
        List<Airport> airports = new ArrayList<>();
        airports.add(Airport.builder().iataCode("BOS").name("Boston - Logan International Airport [BOS]").build());
        airports.add(Airport.builder().iataCode("PSM").name("Boston - Portsmouth International Airport at Pease [PSM]").build());
        airports.add(Airport.builder().iataCode("BST").name("Lashkar Gāh - Bost Airfield [BST]").build());
        assertThat(response.getBody(), containsInAnyOrder(airports.toArray()));
    }

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }

    @Test
    public void givenTermWhenAirportsAutocompleteThenCachedAirports() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String term = "Bost";
        Cache cache = cacheManager.getCache("autocomplete-airports");
        ResponseEntity<List<Airport>> airports = restTemplate.exchange(createUrlWithPort("/airports/autocomplete/" + term), HttpMethod.GET, entity, new ParameterizedTypeReference<List<Airport>>() {
        });
        assertThat(airports.getBody().hashCode(), equalTo(cache.get(term).get().hashCode()));
    }
}
