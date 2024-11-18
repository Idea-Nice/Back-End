package com.example.healax.todolist.repository;

import com.example.healax.todolist.entity.Todolist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodolistRepository extends JpaRepository<Todolist, Long> {

    List<Todolist> findByUser_UserId(String userId);
}
