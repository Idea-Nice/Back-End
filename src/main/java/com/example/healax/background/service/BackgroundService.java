package com.example.healax.background.service;

import com.example.healax.background.domain.Background;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.repository.BackgroundRepository;
import com.example.healax.exception.CustomException;
import com.example.healax.exception.UserNotFoundException;
import com.example.healax.storage.GcsBackgroundService;
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
public class BackgroundService {

    private final GcsBackgroundService gcsBackgroundService;
    private final BackgroundRepository backgroundRepository;
    private final UserRepository userRepository;

    // 모든 배경화면 가져오기
    public List<Background> getAllBackgrounds() {
        return backgroundRepository.findAll();
    }

    // 해당 유저가 보유한 배경화면 가져오기 (Set로 묶어서 전달해줌)
    public Set<Background> getUserOwnedBackgrounds(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return user.getOwnedBackgrounds();
    }


    // 현재 배경화면 설정 변경하기
    public Background setCurrentBackground(String userId, String backgroundName) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        Background background = backgroundRepository.findByName(backgroundName)
                .orElseThrow(() -> new CustomException("해당 배경화면이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        // 현재 유저의 보유 배경화면 세트를 가져와 변경하려는 배경화면이 있는지 검사
        if (!user.getOwnedBackgrounds().contains(background)) {
            throw new CustomException("이 배경화면은 해당 유저가 가진 배경화면이 아닙니다.", HttpStatus.FORBIDDEN);
        }

        user.setCurrentBackground(background);
        userRepository.save(user);

        return background;
    }

    // 현재 적용중인 배경화면 가져오기
    public Optional<Background> getCurrentBackground(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));

        return Optional.ofNullable(user.getCurrentBackground()); // 찾은 유저의 현재배경화면을 Optional로 감싸 반환
    }

    // 배경화면 구매하기 (권한 해제)
    public Background purchaseBackground(String userid, String backgroundName) {
        User user = userRepository.findByUserId(userid)
                .orElseThrow(() -> new UserNotFoundException("해당 유저를 찾을 수 없습니다."));
        Background background = backgroundRepository.findByName(backgroundName)
                .orElseThrow(() -> new CustomException("해당 배경화면이 존재하지 않습니다.", HttpStatus.NOT_FOUND));

        if(user.getOwnedBackgrounds().contains(background)) {
            throw new CustomException("이미 소유한 배경화면 입니다.", HttpStatus.CONFLICT);
        }

        user.addOwnedBackground(background);
        userRepository.save(user);

        return background;
    }

    // 배경화면 업로드 : storage패키지에 구현해둔 파일을 저장하고 저장한 파일의 퍼블릭이미지url을 얻어오는 uploadFile() 호출
    public Background saveBackground(String name, MultipartFile file) throws IOException {
        String imageUrl = gcsBackgroundService.uploadFile(file);

        Background background = new Background();
        background.setName(name);
        background.setUrl(imageUrl);

        return backgroundRepository.save(background);
    }

    // 배경화면 기본 권한 추가 및 현재 배경화면 설정
    public void addDefaultBackground(User user) {

        Background defaultBackground = backgroundRepository.findByName("다락방")
                .orElseThrow(() -> new CustomException("대상 기본 배경화면 <다락방>이 db에 추가되지 않았습니다.", HttpStatus.NOT_FOUND));

        user.addOwnedBackground(defaultBackground);
    }

    // 기본 배경화면 설정
    public void setDefaultBackground(User user) {
        Background defaultBackground = backgroundRepository.findByName("다락방")
                .orElseThrow(() -> new CustomException("대상 기본 배경화면 <다락방>이 db에 추가되지 않았습니다.", HttpStatus.NOT_FOUND));

        user.setDefaultCurrentBackground(defaultBackground);
    }

    // 기본 배경화면 DB에 저장하기
    public BackgroundDTO saveReadyBackground() {

        Background background = new Background();

        background.setName("다락방");
        background.setUrl("https://storage.googleapis.com/healax-background/background/6d20ebb9-47ab-4c69-bc3c-8160fbae7611-fe83b7028f466552.jpg");

        backgroundRepository.save(background);

        BackgroundDTO backgroundDTO = new BackgroundDTO();

        backgroundDTO.setName(background.getName());
        backgroundDTO.setUrl(background.getUrl());

        return backgroundDTO;
    }
}
