package com.example.healax.calender.service;

import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CalenderService {

    private final UserRepository userRepository;
//    private final com.example.healax.Calender.repository.CalenderRepository calenderRepository;

//    // 캘린더 저장하기
//    public void save(Long user_Id, SaveCalenderDTO saveCalenderDTO) {
//        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(user_Id);
//
//        if (userEntityOptional.isPresent()) {
//            UserEntity userEntity = userEntityOptional.get();
//
//            CalenderEntity calenderEntity = new CalenderEntity();
//            calenderEntity.setTitle(saveCalenderDTO.getTitle());
//            calenderEntity.setContent(saveCalenderDTO.getContent());
//            calenderEntity.setStartday(saveCalenderDTO.getStartday());
//            calenderEntity.setEndday(saveCalenderDTO.getEndday());
//            calenderEntity.setUser(userEntity);
//
//            calenderRepository.save(calenderEntity);
//        }
//    }

}