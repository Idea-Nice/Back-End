package com.example.healax.background.repository;

import com.example.healax.background.domain.Background;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    // 이름으로 조회
    Optional<Background> findByName(String name);
}
