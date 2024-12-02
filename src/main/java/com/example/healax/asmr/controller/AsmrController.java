package com.example.healax.asmr.controller;

import com.example.healax.asmr.dto.AsmrDTO;
import com.example.healax.asmr.dto.PurcahseRequestDTO;
import com.example.healax.asmr.service.AsmrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/asmr")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class AsmrController {

    private final AsmrService asmrService;

    // 모든 asmr 가져오기
    @GetMapping("/all")
    public ResponseEntity<List<AsmrDTO>> getAllAsmrs() {
        return ResponseEntity.ok(asmrService.getAllAsmrs());
    }

    // 해당 유저 보유 asmr 가져오기
    @GetMapping("/{userId}/owned")
    public ResponseEntity<Set<AsmrDTO>> getUserOwnedAsmrs(@PathVariable String userId) {
        return ResponseEntity.ok(asmrService.getUserOwnedAsmrs(userId));
    }

    // asmr 결제하기 (권한 추가) - 회원id, asmr 이름으로 조회
    @PostMapping("/purchase")
    public ResponseEntity<AsmrDTO> purchaseAsmr(@RequestBody PurcahseRequestDTO request) {
        AsmrDTO purchasedAsmr = asmrService.purchaseAsmr(request.getUserId(), request.getAsmrName());
        return ResponseEntity.ok(purchasedAsmr);
    }

    // asmr 파일 업로드하기 (개발자기능 - GCS, DB 저장)
    @PostMapping("/upload")
    public ResponseEntity<AsmrDTO> uploadAsmr(@RequestParam("name") String name, @RequestParam("audioFile") MultipartFile audioFile, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(asmrService.saveAsmr(name, audioFile, imageFile));
    }

}
