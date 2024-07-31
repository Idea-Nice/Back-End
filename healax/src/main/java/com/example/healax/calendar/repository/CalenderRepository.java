package com.example.healax.calendar.repository;

import com.example.healax.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalenderRepository extends JpaRepository<Calendar, Long> {
    List<Calendar> findByUserId(String userId);
}
