package com.nearsoft.training.travel.api.service;

import com.nearsoft.training.travel.api.dao.User;
import com.nearsoft.training.travel.api.exception.NoUserFoundException;
import com.nearsoft.training.travel.api.exception.RequiredParametersException;
import com.nearsoft.training.travel.api.exception.UserFoundException;
import com.nearsoft.training.travel.api.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String username, String email, String password) {
        if (Strings.isEmpty(username) || Strings.isEmpty(email) || Strings.isEmpty(password)) {
            throw new RequiredParametersException("Username, email and password are required");
        }
        User user = userRepository.findByUsername(username);
        if (user != null) {
            throw new UserFoundException("Username " + username + " already exists!");
        }
        user = User.builder().username(username).email(email).password(password).build();
        return userRepository.save(user);
    }

    public void unregisterUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new NoUserFoundException("Username " + username + " does not exists!");
        }
        userRepository.deleteById(user.getId());
    }
}
