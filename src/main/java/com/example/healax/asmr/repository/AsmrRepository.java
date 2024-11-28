package com.example.healax.asmr.repository;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsmrRepository extends JpaRepository<Asmr, Long> {
    Optional<Asmr> findByName(String name);
}
