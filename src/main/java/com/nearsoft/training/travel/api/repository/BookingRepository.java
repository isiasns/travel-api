package com.nearsoft.training.travel.api.repository;

import com.nearsoft.training.travel.api.dao.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Long> {
    List<Booking> findByUserId(Long id);
}
