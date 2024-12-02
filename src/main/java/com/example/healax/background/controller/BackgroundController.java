package com.example.healax.background.controller;

import com.example.healax.background.domain.Background;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.dto.BackgroundRequestDTO;
import com.example.healax.background.mapper.BackgroundMapper;
import com.example.healax.background.service.BackgroundService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BackgroundDTO> setCurrentBackground(@RequestBody BackgroundRequestDTO request) {
        BackgroundDTO backgroundDTO = BackgroundMapper.toDTO(backgroundService.setCurrentBackground(request.getUserId(), request.getBackgroundName()));
        return ResponseEntity.ok(backgroundDTO);
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
    public ResponseEntity<BackgroundDTO> purchaseBackground(@RequestBody BackgroundRequestDTO request) {
        return ResponseEntity.ok(
                BackgroundMapper.toDTO(backgroundService.purchaseBackground(request.getUserId(), request.getBackgroundName()))
        );
    }

    // 배경화면 업로드하기 (개발자 기능 - GCS, DB 저장)
    @PostMapping("/upload")
    public ResponseEntity<BackgroundDTO> uploadBackground(@RequestParam("name") String name, @RequestParam("file")MultipartFile file) throws IOException {
        Background background = backgroundService.saveBackground(name, file);
        return ResponseEntity.ok(BackgroundMapper.toDTO(background));
    }
}
