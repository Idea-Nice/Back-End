package com.example.healax.user.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UserLevelService {

    private final UserRepository userRepository;


    //회원 레벨 가져오기
    public Integer getLevel(String user_Id) {
        Optional<User> userEntity = userRepository.findByUserId(user_Id);

        if (userEntity.isPresent()) {
            User user = userEntity.get();
            Integer userLevel = user.getLevel();
            return userLevel;
        } else {
            return null;
        }
    }

    //회원 경험치 추가 로직
    public void addExp(Long user_id) {

        Optional<User> userEntity = userRepository.findById(user_id);
        if (userEntity.isPresent()) {

            User user = userEntity.get();

            user.setExp(user.getExp() + 1);

            if (user.getExp() >= 45) {
                user.setExp(user.getExp() - 45);
                user.setLevel(user.getLevel() + 1);
            }
            userRepository.save(user);
        }
    }


//    public void updateLastLoginTime(Long user_Id) {
//        Optional<User> userOptional = userRepository.findById(user_Id);
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            user.setLastLoginTime(LocalDateTime.now());
//
//            userRepository.save(user);
//        }
//    }

//    // EXP를 추가해야 하는지 확인하는 메서드
//    public boolean shouldAddXp(User user) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime lastLoginTime = user.getLastLoginTime();
//        // 마지막 로그인 시간이 1분 이상 경과했는지 확인
//        return lastLoginTime != null && lastLoginTime.plusMinutes(1).isBefore(now);
//    }


}