package com.example.healax.asmr.service;

import com.example.healax.asmr.entity.Asmr;
import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.asmr.repository.UserAsmrRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Asmr saveFile(MultipartFile file) throws IOException {
        Asmr asmr = new Asmr();
        asmr.setFileName(file.getOriginalFilename());
        asmr.setFileType(file.getContentType());
        asmr.setFilesize(file.getSize());
        asmr.setData(file.getBytes());

        return asmrRepository.save(asmr);
    }

    // 해당 id asmr 오디오 넘겨주기
    public Optional<Asmr> getFile(Long id) {
        return asmrRepository.findById(id);
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
