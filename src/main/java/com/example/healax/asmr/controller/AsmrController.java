package com.example.healax.asmr.controller;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.asmr.dto.AsmrDTO;
import com.example.healax.asmr.service.AsmrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/asmr")
@RequiredArgsConstructor
public class AsmrController {

    private final AsmrService asmrService;

    // 모든 asmr 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<AsmrDTO>> getAllAsmrs() {
        List<AsmrDTO> asmrs = asmrService.getAllAsmrs().stream()
                .map(asmr -> new AsmrDTO(asmr.getId(), asmr.getFileName(), asmr.getUrl(), asmr.getContentType()))
                .toList();

        return ResponseEntity.ok(asmrs);
    }

    // 해당 유저 보유 asmr 가져오기
    @GetMapping("/{userId}/owned")
    public ResponseEntity<Set<AsmrDTO>> getUserOwnedAsmrs(@PathVariable String userId) {
        Set<AsmrDTO> asmrs = asmrService.getUserOwnedAsmrs(userId).stream()
                .map(asmr -> new AsmrDTO(asmr.getId(), asmr.getFileName(), asmr.getUrl(), asmr.getContentType()))
                .collect(Collectors.toSet());
        return ResponseEntity.ok(asmrs);
    }

    // asmr 결제하기 (권한 추가) - background와 달리 asmr 파일 명으로 입력받습니다.
    @PostMapping("/{userId}/purchase/{asmrFileName}")
    public ResponseEntity<AsmrDTO> purchaseAsmr(@PathVariable String userId, @PathVariable String asmrFileName) {
        Asmr asmr = asmrService.purchaseAsmr(userId, asmrFileName);
        AsmrDTO asmrDTO = new AsmrDTO(asmr.getId(), asmr.getFileName(), asmr.getUrl(), asmr.getContentType());

        return ResponseEntity.ok(asmrDTO);
    }

    // asmr 파일 업로드하기 (개발자기능 - GCS, DB 저장)
    @PostMapping("/upload")
    public ResponseEntity<AsmrDTO> uploadAsmr(@RequestParam("file")MultipartFile file) throws IOException {
        Asmr asmr = asmrService.saveAsmr(file);
        AsmrDTO asmrDTO = new AsmrDTO(asmr.getId(), asmr.getFileName(), asmr.getUrl(), asmr.getContentType());

        return ResponseEntity.ok(asmrDTO);
    }


}
