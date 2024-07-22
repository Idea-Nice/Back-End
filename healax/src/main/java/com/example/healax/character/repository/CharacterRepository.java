package com.example.healax.character.repository;

import com.example.healax.character.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    Optional<Character> findById(Long id);

    @Query("SELECT b FROM Character b WHERE b.name = :name")
    List<Character> findByName(@Param("name") String name);

    @Query("SELECT b.id FROM Character b JOIN b.users u WHERE u.userId = :userId")
    List<Long> findCharacterIdsByUserId(@Param("userId") String userId);
}
