// BackgroundController의 DTO 변환로직 반복을 제거하기 위한 매퍼클래스


package com.example.healax.background.mapper;


import com.example.healax.background.domain.Background;
import com.example.healax.background.dto.BackgroundDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class BackgroundMapper {
    // Background -> BackgroundDTO
    public static BackgroundDTO toDTO(Background background) {
        return new BackgroundDTO(background.getId(), background.getName(), background.getUrl());
    }

    // List<Background> -> List<BackgroundDTO>
    public static List<BackgroundDTO> toDTOList(List<Background> backgrounds) {
        return backgrounds.stream().map(BackgroundMapper::toDTO).collect(Collectors.toList());
    }

    // Set<Background> -> Set<BackgroundDTO>
    public static Set<BackgroundDTO> toDTOSet(Set<Background> backgrounds) {
        return backgrounds.stream().map(BackgroundMapper::toDTO).collect(Collectors.toSet());
    }
}
