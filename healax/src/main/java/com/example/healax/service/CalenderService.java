package com.example.healax.service;

import com.example.healax.dto.CalenderDTO;
import com.example.healax.entity.CalenderEntity;
import com.example.healax.entity.UserEntity;
import com.example.healax.repository.CalenderRepository;
import com.example.healax.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CalenderService {

    private final CalenderRepository calenderRepository;

    public List<CalenderDTO> findAll(Long userId) {

        List<CalenderEntity> calenderEntityList = calenderRepository.findAll(userId);
        List<CalenderDTO> calenderDTOList = new ArrayList<>();

        for (CalenderEntity calenderEntity : calenderEntityList) {
            calenderDTOList.add(CalenderDTO.toSaveCalenderDTO(calenderEntity));
        }

        return calenderDTOList;
    }
}