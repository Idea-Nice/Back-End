package com.example.healax.socket.repository;

import com.example.healax.socket.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 특정 발신자와 수신자 간의 모든 메시지 조회 (timestamp 시간순)
    List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimeStampAsc(String senderId, String recipientId, String senderId2, String recipientId2);

}
