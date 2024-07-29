package com.example.healax.bell.service;

import com.example.healax.bell.entity.Bell;
import com.example.healax.bell.repository.BellRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BellService {

    @Autowired
    private BellRepository bellRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Bell> getBellsByUserId(String userId) {
        return bellRepository.findByUser_UserId(userId);
    }

    public Bell saveBell(Bell bell, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(bell::setUser);
        return bellRepository.save(bell);
    }

    public Bell modifyBell(Bell bell, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(bell::setUser);
        return bellRepository.save(bell);
    }

    public void deleteBell(Long bell_id) {
        bellRepository.deleteById(bell_id);
    }

    // 사용 안함. 이거 일정기간마다 실행하는 방법 말고,
    // 프론트에서 확인눌러서 삭제되는걸로 ㄱㄱ
//    public void deleteNonRepeatingAlarms() {
//        List<Bell> bells = bellRepository.findAll();
//        for (Bell bell : bells) {
//            if (bell.getRepeatDays().isEmpty() && bell.getTime().isBefore(LocalDateTime.now())) {
//                deleteBell(bell.getId());
//            }
//        }
//    }
}
