package com.example.healax.playhistory.repository;

import com.example.healax.playhistory.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {
}
