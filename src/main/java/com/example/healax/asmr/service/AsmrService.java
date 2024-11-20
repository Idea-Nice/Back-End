package com.example.healax.asmr.service;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.exception.CustomException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.storage.GcsAsmrService;
import com.example.healax.user.domain.User;
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
    public List<Asmr> getAllAsmrs() {
        return asmrRepository.findAll();
    }

    // 해당 유저가 보유한 asmr 가져오기 (권한이 Set이라 asmr도 Set으로 보냄)
    public Set<Asmr> getUserOwnedAsmrs(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return user.getOwnedAsmr();
    }

    // asmr 결제하기 (권한 해제)
    public Asmr purchaseAsmr(String userId, String asmrFileName) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        Asmr asmr = asmrRepository.findByFileName(asmrFileName)
                .orElseThrow(() -> new CustomException("해당 asmr을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (user.getOwnedAsmr().contains(asmr)) {
            throw new CustomException("이미 소유하고 있는 asmr입니다.", HttpStatus.CONFLICT);
        }

        user.addOwnedAsmr(asmr);
        userRepository.save(user);

        return asmr;
    }

    // asmr 파일 GCS에 업로드
    public Asmr saveAsmr(MultipartFile file) throws IOException {
        String asmrFileName = gcsAsmrService.uploadFile(file);

        Optional<Asmr> asmrOptional = asmrRepository.findByFileName(asmrFileName);

        if (asmrOptional.isEmpty()) {
            throw new CustomException("GCS, DB 저장 과정 중 문제가 발생하여 파일 이름을 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return asmrOptional.get();
    }

}
