package com.example.healax.calender.repository;

import com.example.healax.calender.entity.Calender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalenderRepository extends JpaRepository<Calender, Long> {
    List<Calender> findByUserId(Long userId);
}
