package com.example.healax.bell.repository;

import com.example.healax.bell.entity.Bell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BellRepository extends JpaRepository<Bell, Long> {
    List<Bell> findByUser_UserId(String userId);
}
