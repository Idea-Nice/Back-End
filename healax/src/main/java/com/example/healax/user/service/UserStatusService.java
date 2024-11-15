package com.example.healax.user.service;

import com.example.healax.user.dto.StatusDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    //회원 상태 가져오기
    public String getStatus(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);

        if (userEntity.isPresent()) {
            User user = userEntity.get();

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
        Optional<User> userEntity = userRepository.findByUserId(statusDTO.getUserId());

        if(userEntity.isPresent()) {
            User user = userEntity.get();

            user.setStatus(statusDTO.isStatus());

            userRepository.save(user);
        }
    }
}
