package com.example.healax.service;


import com.example.healax.dto.SaveCalenderDTO;
import com.example.healax.entity.CalenderEntity;
import com.example.healax.entity.UserEntity;
import com.example.healax.repository.CalenderRepository;
import com.example.healax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CalenderService {

    private final UserRepository userRepository;
    private final CalenderRepository calenderRepository;

    // 캘린더 저장하기
    public void save(String user_Id, SaveCalenderDTO saveCalenderDTO) {
        Optional<UserEntity> userEntityOptional = userRepository.findByUserId(user_Id);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            CalenderEntity calenderEntity = new CalenderEntity();
            calenderEntity.setTitle(saveCalenderDTO.getTitle());
            calenderEntity.setContent(saveCalenderDTO.getContent());
            calenderEntity.setStartday(saveCalenderDTO.getStartday());
            calenderEntity.setEndday(saveCalenderDTO.getEndday());
            calenderEntity.setUser(userEntity);

            calenderRepository.save(calenderEntity);
        }
    }

}