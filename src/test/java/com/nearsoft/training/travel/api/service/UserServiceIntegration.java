package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.ApiApplication;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApiApplication.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegration {

    @Autowired
    private UserService userService;

    @Test
    public void givenUsernameEmailPasswordWhenRegisterUserThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User registeredUser = userService.registerUser(username, email, password);
        assertThat(username, equalTo(registeredUser.getUsername()));
    }

    @Test
    public void givenUsernameWhenUnregisterUserThenDeleteUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        userService.registerUser(username, email, password);
        userService.unregisterUser(username);
    }

    @Test
    public void givenUsernamePasswordWhenLoginUserThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User registeredUser = userService.registerUser(username, email, password);
        User loginUser = userService.loginUser(username, password);
        assertThat(registeredUser.getUsername(), equalTo(loginUser.getUsername()));
    }
}
