package com.example.healax.user.service;

import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.Follow;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.FollowRepository;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.flogger.Flogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowService.class);
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
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

    // 특정 사용자의 팔로우한 목록을 가져오는 메서드
    public List<UserDTO> getFollowingUsers(Long userId) {
        List<Follow> following = followRepository.findByFollowerId(userId);
        return following.stream()
                .map(f -> UserDTO.toSaveUserEntityDTO(f.getFollowing()))
                .collect(Collectors.toList());
    }

    // 팔로우 제출 시 호출
    public boolean followUser(Long followerId, Long followingId) {
        try {
            Optional<User> follower = userRepository.findById(followerId);
            Optional<User> following = userRepository.findById(followingId);

            if(follower.isPresent() && following.isPresent()) {
                Follow follow = new Follow();
                follow.setFollower(follower.get());
                follow.setFollowing(following.get());
                followRepository.save(follow);

                logger.info("{}가 {}를 팔로우했습니다.", followerId, followingId);
                return true;
            } else {
                logger.warn("팔로우 하는 사람 ({}) 또는 팔로우 당하는 사람 ({})을 찾을 수 없습니다.", followerId, followingId);
                return false;
            }
        } catch (Exception e) {
            logger.error("{}가 {}를 팔로우 하는 중 에러 발생", followerId, followingId, e);
            return false;
        }
    }

    // 언팔로우 제출 시 호출
    public boolean unfollowUser(Long followerId, Long followingId) {
        try {
            Optional<Follow> follow = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
            if (follow.isPresent()){
                followRepository.delete(follow.get());
                logger.info("{}가 {}를 언팔로우 했습니다.", followerId, followingId);
                return true;
            } else {
                logger.warn("{}가 {}를 팔로우하고 있다는 정보를 찾을 수 없습니다.",followerId, followingId);
                return false;
            }
        } catch (Exception e) {
            logger.error("{}가 {}를 언팔로우 하는 동안 오류 발생", followerId, followingId, e);
            return false;
        }
    }


}
