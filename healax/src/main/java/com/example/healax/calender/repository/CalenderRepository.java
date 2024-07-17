package com.example.healax.calender.repository;

import com.example.healax.calender.entity.CalenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalenderRepository extends JpaRepository<CalenderEntity, Long> {
    List<CalenderEntity> findAllByUserId(Long user_Id);
}
