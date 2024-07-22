package com.example.healax.calender.repository;

import com.example.healax.Calender.entity.CalenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalenderRepository extends JpaRepository<CalenderEntity, Long> {
}
