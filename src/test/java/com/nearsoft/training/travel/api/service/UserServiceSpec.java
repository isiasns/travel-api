package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.NoMatchCredentialsException;
import com.nearsoft.training.travel.api.exception.NoUserFoundException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.exception.UserFoundException;
import com.nearsoft.training.travel.api.repository.UserRepository;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(EasyMockRunner.class)
public class UserServiceSpec {
    @Mock
    private UserRepository userRepository;
    @TestSubject
    private UserService userService = new UserService(userRepository);

    @Test
    public void givenUsernameEmailAndPasswordWhenRegisterUserThenSaveAndReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = User.builder().username(username).email(email).password(password).build();
        expect(userRepository.findByUsername(anyString())).andReturn(null);
        expect(userRepository.save(anyObject())).andReturn(user);
        replay(userRepository);
        User registeredUser = userService.registerUser(username, email, password);
        assertThat(registeredUser, equalTo(user));
        verify(userRepository);
    }

    @Test(expected = UserFoundException.class)
    public void givenUsernameEmailAndPasswordWhenRegisterUserAndUserExistsThenThrowException() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = User.builder().username(username).email(email).password(password).build();
        expect(userRepository.findByUsername(anyString())).andReturn(user);
        replay(userRepository);
        userService.registerUser(username, email, password);
        verify(userRepository);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyUsernameEmailPasswordWhenRegisterUserThenThrowException() {
        userService.registerUser(null, null, null);
    }

    @Test
    public void givenUsernameWhenUnregisterUserThenDeleteUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = User.builder().username(username).email(email).password(password).build();
        expect(userRepository.findByUsername(anyString())).andReturn(user);
        userRepository.deleteById(anyLong());
        expectLastCall().once();
        replay(userRepository);
        userService.unregisterUser(username);
        verify(userRepository);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyUsernameWhenUnregisterUserThenThrowException() {
        userService.unregisterUser(null);
    }

    @Test(expected = NoUserFoundException.class)
    public void givenWrongUsernameWhenUnregisterUserThenThrowException() {
        userService.unregisterUser("wronguser");
    }

    @Test
    public void givenUsernamePasswordWhenLoginUserThenReturnUser() {
        String username = "isiasns";
        String email = "isias@nearsoft.com";
        String password = "12345678";
        User user = User.builder().username(username).email(email).password(password).build();
        expect(userRepository.findByUsernameAndPassword(anyString(), anyString())).andReturn(user);
        replay(userRepository);
        User registeredUser = userService.loginUser(username, password);
        assertThat(registeredUser, equalTo(user));
        verify(userRepository);
    }

    @Test(expected = RequiredParametersException.class)
    public void givenEmptyUsernamePasswordWhenLoginThenThrowException() {
        userService.loginUser(null, null);
    }

    @Test(expected = NoMatchCredentialsException.class)
    public void givenWrongUsernamePasswordWhenLoginThenThrowException() {
        String username = "isiasns";
        String password = "12345678";
        expect(userRepository.findByUsernameAndPassword(anyString(), anyString())).andReturn(null);
        replay(userRepository);
        User registeredUser = userService.loginUser(username, password);
        assertThat(registeredUser, is(nullValue()));
        verify(userRepository);
    }
}
