package com.example.healax.repository;

import com.example.healax.entity.CalenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalenderRepository extends JpaRepository<CalenderEntity, Long> {
    List<CalenderEntity> findAllByUserId(Long user_Id);
}
