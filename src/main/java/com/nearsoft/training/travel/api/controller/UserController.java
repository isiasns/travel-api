package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> registerUser(@RequestBody Map<String, String> userData) {
        if (userData == null || !isValidUserData(userData)) {
            throw new RequiredParametersException("Username, email and password are required");
        }
        return new ResponseEntity<>(userService.registerUser(userData.get("username"), userData.get("email"), userData.get("password")), HttpStatus.OK);
    }

    private boolean isValidUserData(Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");
        return !Strings.isEmpty(username) && !Strings.isEmpty(email) && !Strings.isEmpty(password);
    }
}
