package com.example.healax.user.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLevelService {

    private final UserRepository userRepository;

    //회원 레벨 가져오기
    public String getLevel(Long user_Id) {
        Optional<User> userEntity = userRepository.findById(user_Id);

        if (userEntity.isPresent()) {
            User user = userEntity.get();
            String userLevel = user.getLevel();
            return userLevel;
        } else {
            return null;
        }
    }


}
