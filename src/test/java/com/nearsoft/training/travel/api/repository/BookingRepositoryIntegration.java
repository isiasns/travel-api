package com.nearsoft.training.travel.api.repository;

import com.nearsoft.training.travel.api.dao.Booking;
import com.nearsoft.training.travel.api.dao.Flight;
import com.nearsoft.training.travel.api.dao.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryIntegration {
    private User persistedUser;
    private Booking persistedBooking;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private BookingRepository bookingRepository;

    @Before
    public void setUp() {
        persistedUser = User.builder().username("isiasns").build();
        persistedBooking = Booking.builder().user(persistedUser).departing(Flight.builder().build())
                .returning(Flight.builder().build()).build();
        persistedUser.setId(testEntityManager.persistAndGetId(persistedUser, Long.class));
        persistedBooking.setId(testEntityManager.persistAndGetId(persistedBooking, Long.class));
        testEntityManager.flush();
    }

    @Test
    public void givenUserIdWhenFindByUserIdThenReturnBookings() {
        List<Booking> bookings = bookingRepository.findByUserId(persistedUser.getId());
        assertThat(bookings, hasSize(1));
    }

    @Test
    public void givenBookingIdWhenFindByIdThenReturnBooking() {
        Booking booking = bookingRepository.findById(persistedBooking.getId()).get();
        assertThat(booking.getId(), is(notNullValue()));
    }
}
