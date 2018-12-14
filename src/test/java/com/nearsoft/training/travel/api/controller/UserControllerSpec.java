package com.nearsoft.training.travel.api.controller;

import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.service.UserService;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(EasyMockRunner.class)
public class UserControllerSpec {
    @Mock
    private UserService userService;
    @TestSubject
    private UserController userController = new UserController(userService);

    @Test
    public void givenUsernameEmailPasswordWhenRegisterUserThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = User.builder().username(username).email(email).password(password).build();
        expect(userService.registerUser(anyString(), anyString(), anyString())).andReturn(user);
        replay(userService);
        ResponseEntity<User> result = userController.registerUser(username, email, password);
        verify(userService);
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());
        assertThat(result.getBody().getUsername(), equalTo(username));
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyUsernameEmailPasswordWhenRegisterUserThenThrowException() {
        userController.registerUser(null, null, null);
    }
}
