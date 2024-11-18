package com.example.healax.sticker.repository;

import com.example.healax.sticker.entity.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
    List<Sticker> findByBackgroundId(Long backgroundId);

    // 사용자 소유 스티커 ID 가져오기
    @Query("SELECT s.id FROM Sticker s JOIN s.users u WHERE u.userId = :userId")
    List<Long> findStickerIdsByUserId(@Param("userId") String userId);
}
