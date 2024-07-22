package com.example.healax.socket.repository;

import com.example.healax.socket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRoomIdOrderByTimeStamp(Long roomId);

}
