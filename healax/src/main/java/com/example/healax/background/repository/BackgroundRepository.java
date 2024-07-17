package com.example.healax.background.repository;

import com.example.healax.background.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {
    @Query("SELECT b FROM Background b WHERE b.name = :name")
    List<Background> findByName(@Param("name") String name);
}
