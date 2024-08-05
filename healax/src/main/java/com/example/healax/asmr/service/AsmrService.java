package com.example.healax.asmr.service;

import com.example.healax.asmr.dto.AsmrDTO;
import com.example.healax.asmr.entity.Asmr;
import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.asmr.repository.UserAsmrRepository;
import com.example.healax.sticker.dto.StickerDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsmrService {

    @Autowired
    private AsmrRepository asmrRepository;

    @Autowired
    private UserAsmrRepository userAsmrRepository;

    @Autowired
    private UserRepository userRepository;

    // 새로운 오디오 저장
    public Asmr saveFile(MultipartFile file, MultipartFile image) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("오디오 파일이 null이거나 비어있습니다.");
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 null이거나 비어있습니다.");
        }

        Asmr asmr = new Asmr();
        asmr.setFileName(file.getOriginalFilename());
        asmr.setData(file.getBytes());
        asmr.setImage(image.getBytes());

        return asmrRepository.save(asmr);
    }

    // 해당 id asmr 오디오 넘겨주기
    public Optional<Asmr> getFile(Long id) {
        return asmrRepository.findById(id);
    }

    // 전체 ASMR 파일 목록 조회
    public List<AsmrDTO> getAllAsmrs() {
        List<Asmr> asmrs = asmrRepository.findAll();

        List<AsmrDTO> asmrDTOs = asmrs.stream().map(asmr -> {
            String musicBase64 = asmr.getData() != null ? Base64.getEncoder().encodeToString(asmr.getData()) : null;
            String imageBase64 = asmr.getImage() != null ? Base64.getEncoder().encodeToString(asmr.getImage()) : null;
            return new AsmrDTO(asmr.getId(), asmr.getFileName(), musicBase64, imageBase64);
        }).collect(Collectors.toList());

        return asmrDTOs;
    }

    // 특정 유저 asmr 보유 목록 조회
    public List<Asmr> getAsmrByUser(String userId) {
        Optional<User> user = userRepository.findByUserId(userId);
        if(user.isPresent()) {
            List<UserAsmr> userAsmrs = userAsmrRepository.findByUser(user.get());
            return userAsmrs.stream()
                    .map(UserAsmr::getAsmr)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
    }

    // 특정 유저에게 asmr 파일 사용 권한 부여 (userId(계정)기반)
    public void grantAccessToAsmr(String userId, Long asmrId) {
        Optional<User> user = userRepository.findByUserId(userId);
        Optional<Asmr> asmr = asmrRepository.findById(asmrId);

        if (user.isPresent() && asmr.isPresent()) {
            UserAsmr userAsmr = new UserAsmr();
            userAsmr.setUser(user.get());
            userAsmr.setAsmr(asmr.get());
            userAsmrRepository.save(userAsmr);
        } else {
            throw new IllegalArgumentException("해당 유저 또는 해당 Asmr파일을 찾을 수 없습니다.");
        }
    }
}
