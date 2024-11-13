package com.example.healax.user.repository;

import com.example.healax.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // userId(String)으로 조회하기 위함
    Optional<User> findByUserId(String userId);

}
