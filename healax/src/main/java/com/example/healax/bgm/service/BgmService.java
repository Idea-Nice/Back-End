package com.example.healax.bgm.service;

import com.example.healax.bgm.dto.BgmDTO;
import com.example.healax.bgm.entity.Bgm;
import com.example.healax.bgm.repository.BgmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BgmService {
    private final BgmRepository bgmRepository;

    // 새 브금 저장하기
    @Transactional
    public void saveBgm(String name, String mood, MultipartFile music) throws IOException {
        Bgm bgm = new Bgm();
        bgm.setName(name);
        bgm.setMood(mood);
        bgm.setMusic(music.getBytes());
        bgmRepository.save(bgm);
    }

    // 해당 분위기의 브금들 가져오기
    public List<BgmDTO> findByMood(String mood) {
        List<Bgm> bgms = bgmRepository.findByMood(mood);
        return bgms.stream().map(bgm -> {
            String musicBase64 = Base64.getEncoder().encodeToString(bgm.getMusic());
            return new BgmDTO(bgm.getId(), bgm.getName(), bgm.getMood(), musicBase64);
        }).collect(Collectors.toList());
    }

    // 혹시 몰라서 만들어두는 전체 bgm 가져오기
    public List<BgmDTO> findAll() {
        List<Bgm> bgms = bgmRepository.findAll();
        return bgms.stream().map(bgm -> {
            String musicBase64 = Base64.getEncoder().encodeToString(bgm.getMusic());
            return new BgmDTO(bgm.getId(), bgm.getName(), bgm.getMood(), musicBase64);
        }).collect(Collectors.toList());
    }
}
