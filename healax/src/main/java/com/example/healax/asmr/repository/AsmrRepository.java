package com.example.healax.asmr.repository;

import com.example.healax.asmr.entity.Asmr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsmrRepository extends JpaRepository<Asmr, Long> {
}
