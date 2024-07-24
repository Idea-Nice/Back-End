package com.example.healax.user.repository;

import com.example.healax.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    // 특정 사용자 간의 팔로우 관계가 존재하는지 확인하는 메서드.
    // 맞팔인지 확인하는게아님. 팔로우 일방적 팔로우 관계가 있는지를 체크하는거임.
    // 이 메서드를 두번 호출(나 vs 상대, 상대 vs 나)해서 맞팔인지 확인함.
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
