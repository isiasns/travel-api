package com.nearsoft.training.travel.api.repository;

import com.nearsoft.training.travel.api.dao.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
