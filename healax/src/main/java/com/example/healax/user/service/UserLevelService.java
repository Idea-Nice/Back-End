package com.example.healax.user.service;

import com.example.healax.asmr.entity.Asmr;
import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.asmr.repository.UserAsmrRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class UserLevelService {

    private final UserRepository userRepository;
    private final UserAsmrRepository userAsmrRepository;
    private final AsmrRepository asmrRepository;

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

    @Transactional
    //회원 경험치 추가 로직
    public void addExp(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            user.setExp(user.getExp() + 1);

            if (user.getExp() >= 45) {
                user.setExp(user.getExp() - 45);
                user.setLevel(user.getLevel() + 1);
                grantAccessToNewAsmrs(user);
            }
            userRepository.save(user);
        }
    }

    // 새 ASMR 접근 권한 부여 로직
    private void grantAccessToNewAsmrs(User user) {
        int newLevel = user.getLevel();

        if(newLevel == 1) {
            unlockAsmrForUser(user, 1L);
        }

        // 3, 5, 7, 9, 11, 13, 15레벨에 asmr 음원 잠금 해제
        if(newLevel >= 3 && newLevel % 2 == 1 && newLevel <= 15) {
            long asmrId = (newLevel / 2) + 1;
            unlockAsmrForUser(user, asmrId);
        }
    }

    private void unlockAsmrForUser(User user, Long asmrId) {
        Optional<Asmr> asmr = asmrRepository.findById(asmrId);

        if(asmr.isPresent()) {
            boolean alreadyUnlocked = user.getUserAsmrs().stream()
                    .anyMatch(userAsmr -> userAsmr.getAsmr().getId().equals(asmrId));

            if(!alreadyUnlocked) {
                UserAsmr userAsmr = new UserAsmr();

                userAsmr.setUser(user);
                userAsmr.setAsmr(asmr.get());
                userAsmrRepository.save(userAsmr);
            }
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