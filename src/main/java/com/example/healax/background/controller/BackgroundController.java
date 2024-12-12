package com.example.healax.background.controller;

import com.example.healax.background.domain.Background;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.dto.BackgroundRequestDTO;
import com.example.healax.background.mapper.BackgroundMapper;
import com.example.healax.background.service.BackgroundService;
import com.example.healax.exception.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/background")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class BackgroundController {

    private final BackgroundService backgroundService;

    // 모든 배경화면 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<BackgroundDTO>> getAllBackgrounds() {
        return ResponseEntity.ok(
                BackgroundMapper.toDTOList(backgroundService.getAllBackgrounds())
        );
    }

    // 해당 유저 보유 배경화면들 불러오기
    @GetMapping("/{userId}/owned")
    public ResponseEntity<Set<BackgroundDTO>> getUserOwnedBackground(@PathVariable String userId) {
        return ResponseEntity.ok(
                BackgroundMapper.toDTOSet(backgroundService.getUserOwnedBackgrounds(userId))
        );
    }

    // 배경화면 설정하기
    @PostMapping("/set-current")
    public ResponseEntity<CommonResponse> setCurrentBackground(@RequestBody BackgroundRequestDTO request) {
        Background background = backgroundService.setCurrentBackground(request.getUserId(), request.getBackgroundName());
        CommonResponse response = new CommonResponse(
                "현재 배경화면이 성공적으로 변경되었습니다. 설정된 배경화면 : " + background.getName(),
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 현재 적용된 배경화면 가져오기
    @GetMapping("/{userId}/current")
    public ResponseEntity<BackgroundDTO> getCurrentBackground(@PathVariable String userId) {
        return backgroundService.getCurrentBackground(userId)
                .map(BackgroundMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 배경화면 결제하기 (권한 추가)
    @PostMapping("/purchase")
    public ResponseEntity<CommonResponse> purchaseBackground(@RequestBody BackgroundRequestDTO request) {
        Background background = backgroundService.purchaseBackground(request.getUserId(), request.getBackgroundName());

        CommonResponse response = new CommonResponse(
                "배경화면 권한을 성공적으로 획득했습니다. 획득한 배경화면 : " + background.getName(),
                200
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 배경화면 업로드하기 (개발자 기능 - GCS, DB 저장)
    @PostMapping("/upload")
    public ResponseEntity<BackgroundDTO> uploadBackground(@RequestParam("name") String name, @RequestParam("file")MultipartFile file) throws IOException {
        Background background = backgroundService.saveBackground(name, file);
        return ResponseEntity.ok(BackgroundMapper.toDTO(background));
    }

    // 기본 배경화면 DB에 저장하기
    @PostMapping("/ready")
    public ResponseEntity<BackgroundDTO> readyBackground() {

        BackgroundDTO backgroundDTO = backgroundService.saveReadyBackground();

        return ResponseEntity.ok(backgroundDTO);
    }
}
