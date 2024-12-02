package com.example.healax.asmr.service;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.asmr.dto.AsmrDTO;
import com.example.healax.asmr.mapper.AsmrMapper;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.exception.CustomException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.storage.GcsAsmrService;
import com.example.healax.user.domain.User;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AsmrService {
    // 의존성 주입
    private final UserRepository userRepository;
    private final AsmrRepository asmrRepository;
    private final GcsAsmrService gcsAsmrService;

    // 모든 asmr 가져오기
    public List<AsmrDTO> getAllAsmrs() {
        return AsmrMapper.toDTOList(asmrRepository.findAll());
    }

    // 해당 유저가 보유한 asmr 가져오기 (권한이 Set이라 asmr도 Set으로 보냄)
    public Set<AsmrDTO> getUserOwnedAsmrs(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return AsmrMapper.toDTOSet(user.getOwnedAsmr());
    }

    // asmr 결제하기 (권한 해제)
    public AsmrDTO purchaseAsmr(String userId, String asmrName) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        Asmr asmr = asmrRepository.findByName(asmrName)
                .orElseThrow(() -> new CustomException("해당 asmr을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (user.getOwnedAsmr().contains(asmr)) {
            throw new CustomException("이미 소유하고 있는 asmr입니다.", HttpStatus.CONFLICT);
        }

        user.addOwnedAsmr(asmr);
        userRepository.save(user);

        return AsmrMapper.toDTO(asmr);
    }

    // asmr 파일 GCS에 업로드
    public AsmrDTO saveAsmr(String name, MultipartFile audioFile, MultipartFile imageFile) throws IOException {
        String audioUrl = gcsAsmrService.uploadAudio(audioFile);
        String imageUrl = gcsAsmrService.uploadImage(imageFile);

        Asmr asmr = new Asmr();
        asmr.setName(name);
        asmr.setAudioFileName(audioFile.getOriginalFilename());
        asmr.setAudioUrl(audioUrl);
        asmr.setAudioContentType(audioFile.getContentType());
        asmr.setImageFileName(imageFile.getOriginalFilename());
        asmr.setImageUrl(imageUrl);
        asmr.setImageContentType(imageFile.getContentType());

        asmrRepository.save(asmr);
        return AsmrMapper.toDTO(asmr);
    }

    // 기본 asmr 권한 추가
    public void addDefaultAsmrs(User user) {

        Optional<Asmr> optionalDefaultAsmr1 = asmrRepository.findByName("빗소리");
        Optional<Asmr> optionalDefaultAsmr2 = asmrRepository.findByName("바람소리");

        if(optionalDefaultAsmr1.isPresent() && optionalDefaultAsmr2.isPresent()) {
            user.addOwnedAsmr(optionalDefaultAsmr1.get());
            user.addOwnedAsmr(optionalDefaultAsmr2.get());
        } else {
            throw new CustomException("기본 Asmr 데이터가 존재하지 않습니다.(빗소리 또는 바람소리)", HttpStatus.NOT_FOUND);
        }
    }
}
