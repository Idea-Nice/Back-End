package com.example.healax.calendar.repository;

import com.example.healax.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {

    Optional<List<Calendar>> findAllByUser_UserId(String userId);
}
