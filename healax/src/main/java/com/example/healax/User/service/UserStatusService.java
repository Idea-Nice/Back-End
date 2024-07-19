package com.example.healax.User.service;

import com.example.healax.User.dto.StatusDTO;
import com.example.healax.User.entity.User;
import com.example.healax.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    //회원 상태 가져오기
    public String getStatus(Long userId) {
        Optional<User> userEntity = userRepository.findById(userId);

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
        Optional<User> userEntity = userRepository.findById(statusDTO.getUserId());

        if(userEntity.isPresent()) {
            User user = userEntity.get();

            user.setStatus(statusDTO.isStatus());

            userRepository.save(user);
        }
    }
}
