package com.example.healax.asmr.mapper;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.asmr.dto.AsmrDTO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AsmrMapper {

    // 단일 Asmr -> AsmrDTO
    public static AsmrDTO toDTO(Asmr asmr) {
        return new AsmrDTO(
                asmr.getId(),
                asmr.getName(),
                asmr.getAudioFileName(),
                asmr.getAudioUrl(),
                asmr.getAudioContentType(),
                asmr.getImageFileName(),
                asmr.getImageUrl(),
                asmr.getImageContentType()
        );
    }

    // Asmr List -> DTO List
    public static List<AsmrDTO> toDTOList(List<Asmr> asmrs) {
        return asmrs.stream().map(AsmrMapper::toDTO).toList();
    }

    // Asmr Set -> DTO Set
    public static Set<AsmrDTO> toDTOSet(Set<Asmr> asmrs) {
        return asmrs.stream().map(AsmrMapper::toDTO).collect(Collectors.toSet());
    }


}
