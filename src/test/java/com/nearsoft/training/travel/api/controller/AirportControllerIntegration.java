package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.ApiApplication;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AirportControllerIntegration {
    private static final String host = "http://localhost:";
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate();

    @Test
    public void givenTermWhenAirportsAutocompleteThenReturnAirports() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        String term = "Bost";
        ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort("/airports/autocomplete/" + term), HttpMethod.GET, entity, String.class);
        String expected = "[ { \"iataCode\": \"BOS\", \"name\": \"Boston - Logan International Airport [BOS]\" }, { \"iataCode\": \"PSM\", \"name\": \"Boston - Portsmouth International Airport at Pease [PSM]\" }, { \"iataCode\": \"BST\", \"name\": \"Lashkar Gāh - Bost Airfield [BST]\" } ]";
        try {
            JSONAssert.assertEquals(expected, response.getBody(), false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }
}
