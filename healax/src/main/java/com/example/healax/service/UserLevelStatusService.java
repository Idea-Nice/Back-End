package com.example.healax.service;

import com.example.healax.config.LevelResponse;
import com.example.healax.dto.StatusDTO;
import com.example.healax.entity.UserEntity;
import com.example.healax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLevelStatusService {

    private final UserRepository userRepository;

    //회원 레벨 가져오기
    public String getLevel(String userId) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);

        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();
            String userLevel = user.getLevel();
            return userLevel;
        } else {
            return null;
        }
    }

    //회원 상태 가져오기
    public String getStatus(String userId) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(userId);

        if (userEntity.isPresent()) {
            UserEntity user = userEntity.get();

            if(user.isStatus() == true) {
                return "active";
            } else {
                return "inactive";
            }
        } else {
            return null;
        }
    }

    //회원 상태 저장하기
    public void save(StatusDTO statusDTO) {
        Optional<UserEntity> userEntity = userRepository.findByUserId(statusDTO.getUserId());

        if(userEntity.isPresent()) {
            UserEntity user = userEntity.get();

            user.setStatus(statusDTO.isStatus());

            userRepository.save(user);
        }
    }
}
