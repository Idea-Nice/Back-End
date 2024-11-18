package com.example.healax.background.repository;

import com.example.healax.background.entity.Background;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BackgroundRepository extends JpaRepository<Background, Long> {

    Optional<Background> findById(Long id);

    @Query("SELECT b FROM Background b WHERE b.name = :name")
    List<Background> findByName(@Param("name") String name);

    @Query("SELECT b.id FROM Background b JOIN b.users u WHERE u.userId = :userId")
    List<Long> findBackgroundIdsByUserId(@Param("userId") String userId);
}
