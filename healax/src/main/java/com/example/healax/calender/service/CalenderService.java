package com.example.healax.calender.service;

import com.example.healax.calender.dto.CalenderDTO;
import com.example.healax.calender.entity.CalenderEntity;
import com.example.healax.calender.repository.CalenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;

    //사용자의 캘린더 목록 반환
    public List<CalenderDTO> getCalenderAllById(Long user_Id) {

        List<CalenderEntity> calenderEntities = calenderRepository.findAllByUserId(user_Id);

        // CalenderEntity 리스트를 CalenderDTO 리스트로 변환합니다.
        List<CalenderDTO> calenderDTOList = new ArrayList<>();

        for (CalenderEntity calenderEntity : calenderEntities) {
            CalenderDTO calenderDTO = CalenderDTO.toSaveCalenderDTO(calenderEntity);
            calenderDTOList.add(calenderDTO);
        }

        // CalenderDTO 리스트를 반환합니다.
        return calenderDTOList;
    }
}
