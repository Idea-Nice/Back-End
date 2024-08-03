package com.example.healax.user.service;

import com.example.healax.asmr.entity.Asmr;
import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.asmr.repository.UserAsmrRepository;
import com.example.healax.background.service.BackgroundService;
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
    private final BackgroundService backgroundService;

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
                backgroundService.grantBackgroundAccessByLevel(user);
            }
            userRepository.save(user);
        }
    }

    // 새 ASMR 접근 권한 부여 로직
    private void grantAccessToNewAsmrs(User user) {
        int userLevel = user.getLevel();
        for (int i = 1; i <= (userLevel + 1) / 2; i++) {
            unlockAsmrForUser(user, (long) i);
        }
    }

    private void unlockAsmrForUser(User user, Long asmrId) {
        Optional<Asmr> asmr = asmrRepository.findById(asmrId);

        if (asmr.isPresent()) {
            boolean alreadyUnlocked = user.getUserAsmrs().stream()
                    .anyMatch(userAsmr -> userAsmr.getAsmr().getId().equals(asmrId));

            if (!alreadyUnlocked) {
                UserAsmr userAsmr = new UserAsmr();
                userAsmr.setUser(user);
                userAsmr.setAsmr(asmr.get());

                // 디버깅 메시지 출력
                System.out.println("Unlocking ASMR for User: " + user.getUserId() + " ASMR ID: " + asmrId);

                userAsmrRepository.save(userAsmr);
            }
        }
    }

    // 현재 레벨 상태를 확인하고 접근권한 알맞게 갱신하는 메서드
    @Transactional
    public void refreshAccess(String userId) {
        Optional<User> userEntity = userRepository.findByUserId(userId);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            grantAccessToNewAsmrs(user);
            backgroundService.grantBackgroundAccessByLevel(user);
        } else {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다. userId : " + userId);
        }
    }

    // 사용자 레벨 임의조정 + 권한갱신까지 한번에
    @Transactional
    public void adjustUserLevel(String userId, int newLevel) {
        Optional<User> userEntity = userRepository.findByUserId(userId);
        if (userEntity.isPresent()) {
            User user = userEntity.get();
            user.setLevel(newLevel);
            userRepository.save(user);
            refreshAccess(userId);
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId : " + userId);
        }
    }
}