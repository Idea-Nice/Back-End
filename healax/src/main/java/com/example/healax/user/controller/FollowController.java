package com.example.healax.user.controller;

import com.example.healax.config.CommonResponse;
import com.example.healax.user.dto.FollowRequest;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.repository.FollowRepository;
import com.example.healax.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;

    // 팔로우한 목록 가져오기
    @GetMapping("/user/following/{userId}")
    public ResponseEntity<CommonResponse> getFollowing(@PathVariable Long userId) {
        List<UserDTO> followingUsers = followService.getFollowingUsers(userId);
        CommonResponse response;

        if(followingUsers != null) {
            response = new CommonResponse(200, HttpStatus.OK, "팔로잉 목록 가져오기 성공", followingUsers);
        } else {
            response = new CommonResponse(400, HttpStatus.BAD_REQUEST, "팔로잉 목록 가져오기 실패", null);
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // 다른 사람 팔로우하기
    @PostMapping("/user/follow")
    public ResponseEntity<CommonResponse> followUser(@RequestBody FollowRequest followRequest) {
        boolean success = followService.followUser(followRequest.getFollowerId(), followRequest.getFollowingId());
        CommonResponse response;

        if(success) {
            response = new CommonResponse(200, HttpStatus.OK, "팔로우 성공", null);
        } else {
            response = new CommonResponse(400, HttpStatus.BAD_REQUEST, "팔로우 실패", null);
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }

    // 해당 유저 언팔로우하기
    @DeleteMapping("/user/unfollow")
    public ResponseEntity<CommonResponse> unfollowUser(@RequestBody FollowRequest followRequest) {
        boolean success = followService.unfollowUser(followRequest.getFollowerId(), followRequest.getFollowingId());
        CommonResponse response;

        if(success) {
            response = new CommonResponse(200, HttpStatus.OK, "언팔로우 성공", null);
        } else {
            response = new CommonResponse(400, HttpStatus.BAD_REQUEST, "언팔로우 실패", null);
        }

        return new ResponseEntity<>(response, response.getHttpStatus());
    }
}
