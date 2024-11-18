package com.example.healax.asmr.repository;

import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAsmrRepository extends JpaRepository<UserAsmr, Long> {
    List<UserAsmr> findByUser(User user);
}
