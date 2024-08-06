package com.example.healax.asmr.controller;

import com.example.healax.asmr.dto.AsmrDTO;
import com.example.healax.asmr.entity.Asmr;
import com.example.healax.asmr.service.AsmrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asmr")
@CrossOrigin(origins = "http://localhost:3000/", allowedHeaders = "*")
public class AsmrController {

    @Autowired
    private AsmrService asmrService;

    // 음원파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAsmrAudioFile(@RequestParam("file") MultipartFile file, @RequestParam("image") MultipartFile image, @RequestParam String fileName) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("오디오 파일이 null이거나 비어있습니다.");
            }
            if (image == null || image.isEmpty()) {
                return ResponseEntity.badRequest().body("이미지 파일이 null이거나 비어있습니다.");
            }
            if (fileName == null || fileName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("파일 이름이 null이거나 비어있습니다.");
            }

            Asmr asmr = asmrService.saveFile(file, image, fileName);
            return ResponseEntity.ok("ASMR 음원파일이 성공적으로 업로드되었습니다. : " + asmr.getId());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("ASMR 음원파일 업로드 실패 : " + e.getMessage());
        } catch (MultipartException e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("멀티파트 요청이 아닙니다: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다 : " + e.getMessage());
        }
    }

    //음원파일 받아오기(id로)
    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadAsmrAudioFile(@PathVariable Long id) {
        Optional<Asmr> asmrAudioFileOptional = asmrService.getFile(id);

        if (asmrAudioFileOptional.isPresent()) {
            Asmr asmr = asmrAudioFileOptional.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = \"" + asmr.getFileName() + "\"")
                    .body(new ByteArrayResource(asmr.getData()));
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    // 전체 ASMR 받아오기
    @GetMapping("/files")
    public ResponseEntity<List<AsmrDTO>> getAllAsmrs() {
        List<AsmrDTO> asmrs = asmrService.getAllAsmrs();
        return ResponseEntity.ok(asmrs);
    }

    // 해당 유저가 사용 가능한 asmr 목록 조회
    @GetMapping("/user/{userId}/files")
    public ResponseEntity<List<Asmr>> getAsmrsByUser (@PathVariable String userId) {
        List<Asmr> asmrs = asmrService.getAsmrByUser(userId);
        return ResponseEntity.ok(asmrs);
    }

    // 유저에게 ASMR 권한 부여
    @PostMapping("/user/{userId}/grant/{asmrId}")
    public ResponseEntity<String> grantAccessToAsmr(@PathVariable String userId, @PathVariable Long asmrId) {
        try{
            asmrService.grantAccessToAsmr(userId, asmrId);
            return ResponseEntity.ok("유저에게 asmr 사용권한이 부여되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다 : " + e.getMessage());
        }
    }
}
