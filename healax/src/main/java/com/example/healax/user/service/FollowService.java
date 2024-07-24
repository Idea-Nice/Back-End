package com.example.healax.user.service;

import com.example.healax.user.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);
    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    // 두 사용자가 서로를 맞팔하고 있는지 확인하는 메서드
    public boolean isMutualFollow(Long followerId, Long followingId) {
        try {
            boolean mutualFollow = followRepository.existsByFollowerIdAndFollowingId(followerId, followingId) &&
                    followRepository.existsByFollowerIdAndFollowingId(followingId, followerId);
            logger.debug("Mutual follow status between {} and {} : {}", followerId, followingId, mutualFollow);
            return mutualFollow;
        } catch (Exception e) {
            logger.error("Error checking mutual follow status between {} and {}", followerId, followingId, e);
            return false;
        }
    }
}
