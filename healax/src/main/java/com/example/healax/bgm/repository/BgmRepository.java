package com.example.healax.bgm.repository;

import com.example.healax.bgm.entity.Bgm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BgmRepository extends JpaRepository<Bgm, Long> {
    List<Bgm> findByMood(String mood);
}
