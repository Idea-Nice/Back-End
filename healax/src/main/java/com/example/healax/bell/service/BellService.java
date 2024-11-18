package com.example.healax.bell.service;

import com.example.healax.bell.dto.BellDTO;
import com.example.healax.bell.entity.Bell;
import com.example.healax.bell.repository.BellRepository;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BellService {

    @Autowired
    private BellRepository bellRepository;

    @Autowired
    private UserRepository userRepository;



    public List<BellDTO> getBellsByUserId(String userId) {
        List<Bell> bells = bellRepository.findByUser_UserId(userId);
        return bells.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BellDTO saveBell(Bell bell, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(bell::setUser);
        Bell savedBell = bellRepository.save(bell);
        return convertToDTO(savedBell);
    }

    // bell -> bellDTO 메서드
    private BellDTO convertToDTO(Bell bell){
        BellDTO bellDTO = new BellDTO();
        bellDTO.setId(bell.getId());
        bellDTO.setTitle(bell.getTitle());
        bellDTO.setTime(bell.getTime());
        bellDTO.setRepeatDays(bell.getRepeatDays());
        bellDTO.setUser(convertUserToDTO(bell.getUser()));
        return bellDTO;
    }

    // User -> UserDTO 메서드
    private UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        return userDTO;
    }

    public BellDTO modifyBell(Bell bell, String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        user.ifPresent(bell::setUser);
        Bell savedBell = bellRepository.save(bell);
        return convertToDTO(savedBell);
    }

    public void deleteBell(Long bell_id) {
        bellRepository.deleteById(bell_id);
    }

    public void deleteManyBells(List<Long> bellIds) {
        bellRepository.deleteAllById(bellIds);
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
