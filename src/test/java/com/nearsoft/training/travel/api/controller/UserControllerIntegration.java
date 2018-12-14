package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegration {
    private static final String host = "http://localhost:";
    @LocalServerPort
    private int port;
    private HttpHeaders headers = new HttpHeaders();
    private TestRestTemplate restTemplate = new TestRestTemplate();

    private String createUrlWithPort(String uri) {
        return host + port + uri;
    }

    @Test
    public void givenUserDataWhenUsersAddThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("password", password);
        HttpEntity<Map> entity = new HttpEntity<>(params, headers);
        ResponseEntity<User> response = restTemplate.exchange(createUrlWithPort("/users/add/"), HttpMethod.POST, entity, new ParameterizedTypeReference<User>() {
        });
        assertThat(response.getBody().getUsername(), equalTo(username));
    }

    @Test
    public void givenEmptyUserDataWhenUsersAddThenReturnBadRequest() {
        Map<String, String> params = new HashMap<>();
        HttpEntity<Map> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(createUrlWithPort("/users/add/"), HttpMethod.POST, entity, String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }
}
