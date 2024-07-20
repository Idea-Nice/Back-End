package com.example.healax.calender.service;


import com.example.healax.calender.dto.CalenderDTO;
import com.example.healax.calender.dto.SaveCalenderDTO;
import com.example.healax.calender.entity.Calender;
import com.example.healax.calender.repository.CalenderRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;
    private final UserRepository userRepository;

    //캘린더 저장
    public void save(Long user_id, SaveCalenderDTO saveCalenderDTO) {

        Optional<User> userEntity = userRepository.findById(user_id);

        if (userEntity.isPresent()) {
            Calender calenderEntity = new Calender();

            calenderEntity.setUser(userEntity.get());
            calenderEntity.setTitle(saveCalenderDTO.getTitle());
            calenderEntity.setContent(saveCalenderDTO.getContent());
            calenderEntity.setStartday(saveCalenderDTO.getStartday());
            calenderEntity.setEndday(saveCalenderDTO.getEndday());

            calenderRepository.save(calenderEntity);
        }
    }

    // 캘린더 수정
    public void update(Long user_Id, Long calender_Id, SaveCalenderDTO saveCalenderDTO) {

        List<Calender> calenderList = calenderRepository.findByUserId(user_Id);

        // 수정하고 싶은 캘린더를 찾음
        for (Calender calender : calenderList) {
            if (calender.getId().equals(calender_Id)) {
                // SaveCalenderDTO의 값을 이용해 캘린더 정보를 수정함
                calender.setTitle(saveCalenderDTO.getTitle());
                calender.setContent(saveCalenderDTO.getContent());
                calender.setStartday(saveCalenderDTO.getStartday());
                calender.setEndday(saveCalenderDTO.getEndday());
                // 기타 필요한 필드들 추가

                // 수정된 캘린더를 저장함
                calenderRepository.save(calender);
            }
        }

    }

    // 캘린더 삭제
    public void delete(Long userId, Long calenderId) {
        List<Calender> calenderList = calenderRepository.findByUserId(userId);

        // 삭제하고 싶은 캘린더를 찾음
        for (Calender calender : calenderList) {
            if (calender.getId().equals(calenderId)) {
                // 해당 캘린더를 삭제함
                calenderRepository.delete(calender);
            }
        }
    }

    // 해당 유저 캘린더 리스트 가져오기
    public List<CalenderDTO> getCalenderList(Long user_Id) {
        List<Calender> calenderList = calenderRepository.findByUserId(user_Id);
        List<CalenderDTO> calenderDTOList = new ArrayList<>();

        for (Calender calender : calenderList) {
            CalenderDTO calenderDTO = new CalenderDTO();
            calenderDTO.setTitle(calender.getTitle());
            calenderDTO.setContent(calender.getContent());
            calenderDTO.setStartday(calender.getStartday());
            calenderDTO.setEndday(calender.getEndday());
            calenderDTOList.add(calenderDTO);
        }

        return calenderDTOList;
    }
}