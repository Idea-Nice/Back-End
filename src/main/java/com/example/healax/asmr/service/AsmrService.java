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

    // 기본 Asmr 강제 DB 적용
    public AsmrDTO saveReady1Asmr() {

        Asmr asmr = new Asmr();

        asmr.setName("빗소리");
        asmr.setAudioFileName("rain-sound-188158.mp3");
        asmr.setAudioUrl("https://storage.googleapis.com/download/storage/v1/b/healax-asmr/o/asmr_audio%2Fce72bb83-7712-4733-b554-f42f2d24bfae_rain-sound-188158.mp3?generation=1733125411025266&alt=media");
        asmr.setAudioContentType("audio/mpeg");
        asmr.setImageFileName("비사진.jpg");
        asmr.setImageUrl("https://storage.googleapis.com/download/storage/v1/b/healax-asmr/o/asmr_image%2F311bc426-badc-41df-a0e2-a9a96598ba2f_비사진.jpg?generation=1733125411118547&alt=media");
        asmr.setImageContentType("image/jpeg");
        asmrRepository.save(asmr);

        return AsmrMapper.toDTO(asmr);
    }

    public AsmrDTO saveReady2Asmr() {

        Asmr asmr = new Asmr();

        asmr.setName("바람소리");
        asmr.setAudioFileName("windrauschen-142720.mp3");
        asmr.setAudioUrl("https://storage.googleapis.com/download/storage/v1/b/healax-asmr/o/asmr_audio%2Ff6a5b583-fef4-4b4a-832c-97e342fa1494_windrauschen-142720.mp3?generation=1733125473348905&alt=media");
        asmr.setAudioContentType("audio/mpeg");
        asmr.setImageFileName("바람사진.jpg");
        asmr.setImageUrl("https://storage.googleapis.com/download/storage/v1/b/healax-asmr/o/asmr_image%2F420d4034-48ac-4fd4-9472-1c24ae53e575_바람사진.jpg?generation=1733125473415957&alt=media");
        asmr.setImageContentType("image/jpeg");
        asmrRepository.save(asmr);

        return AsmrMapper.toDTO(asmr);
    }
}
