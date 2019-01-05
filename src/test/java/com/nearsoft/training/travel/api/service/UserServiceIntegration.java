package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
public class UserServiceIntegration {
    @Autowired
    private UserService userService;

    @Test
    public void givenUsernameEmailPasswordWhenRegisterUserThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = userService.registerUser(username, email, password);
        assertThat(username, equalTo(user.getUsername()));
    }

    @Test
    public void givenUsernameWhenUnregisterUserThenDeleteUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = userService.registerUser(username, email, password);
        userService.unregisterUser(username);
    }
}
