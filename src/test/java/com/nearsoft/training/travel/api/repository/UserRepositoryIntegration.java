package com.nearsoft.training.travel.api.repository;

import com.nearsoft.training.travel.api.dao.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegration {
    private User persistedUser = null;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;


    @Before
    public void setUp() {
        persistedUser = User.builder().username("isias").password("12345678").build();
        testEntityManager.persist(persistedUser);
        testEntityManager.flush();
    }

    @Test
    public void givenUsernameWhenFindByUsernameThenReturnUser() {
        User user = userRepository.findByUsername("isias");
        assertThat(user.getId(), equalTo(persistedUser.getId()));
    }
}
