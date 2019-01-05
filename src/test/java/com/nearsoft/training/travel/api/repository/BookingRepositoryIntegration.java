package com.nearsoft.training.travel.api.repository;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryIntegration {
    private User persistedUser;
    private Booking persistedBooking;
    private Flight persistedDeparting;
    private Flight persistedReturning;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Before
    public void setUp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        persistedUser = User.builder().username("isiasns").password("12345678").email("isias@nearsoft.com").build();
        try {
            persistedDeparting = Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build();
            persistedReturning = Flight.builder().origin("LAX").originTerminal("7").destination("BOS").destinationTerminal("B").departureDate(dateFormat.parse("2019-01-31T08:15")).arrivalDate(dateFormat.parse("2019-01-31T16:42")).number("824").airline("UA").build();
            persistedBooking = Booking.builder().user(persistedUser)
                    .departing(persistedDeparting)
                    .returning(persistedReturning)
                    .build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        persistedDeparting.setId(testEntityManager.persistAndGetId(persistedDeparting, Long.class));
        persistedReturning.setId(testEntityManager.persistAndGetId(persistedReturning, Long.class));
        persistedUser.setId(testEntityManager.persistAndGetId(persistedUser, Long.class));
        persistedBooking.setId(testEntityManager.persistAndGetId(persistedBooking, Long.class));
        testEntityManager.flush();
    }

    @Test
    public void givenUserIdWhenFindByUserIdThenReturnBookings() {
        List<Booking> bookings = bookingRepository.findByUserId(persistedUser.getId());
        assertThat(bookings, hasSize(1));
    }

    @After
    public void tearDown() {
        testEntityManager.remove(persistedBooking);
        testEntityManager.remove(persistedUser);
        testEntityManager.remove(persistedDeparting);
        testEntityManager.remove(persistedReturning);
    }
}
