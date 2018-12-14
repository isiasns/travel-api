package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ResponseEntity<User> registerUser(String username, String email, String password) {
        if (Strings.isEmpty(username) || Strings.isEmpty(email) || Strings.isEmpty(password)) {
            throw new RequiredParametersException("Username, email and password are required");
        }
        return new ResponseEntity<>(userService.registerUser(username, email, password), HttpStatus.OK);
    }
}
