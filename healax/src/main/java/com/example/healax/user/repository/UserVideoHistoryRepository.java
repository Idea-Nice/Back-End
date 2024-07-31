package com.example.healax.user.repository;

import com.example.healax.user.entity.UserVideoHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVideoHistoryRepository extends JpaRepository<UserVideoHistory, Long> {
}
