package com.example.healax.sticker.controller;

import com.example.healax.config.CommonResponse;
import com.example.healax.sticker.dto.StickerDTO;
import com.example.healax.sticker.entity.Sticker;
import com.example.healax.sticker.service.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sticker")
@CrossOrigin(origins = "http://43.203.68.91:80/", allowedHeaders = "*")
public class StickerController {

    private final StickerService stickerService;

    // 스티커 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<CommonResponse> uploadSticker(@RequestParam("name") String name,
                                                        @RequestParam("image") MultipartFile image,
                                                        @RequestParam("backgroundId") Long backgroundId,
                                                        @RequestParam("pos_left") int pos_left,
                                                        @RequestParam("pos_top") int pos_top) throws IOException {
        stickerService.saveSticker(name, image, backgroundId, pos_left, pos_top);
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "스티커 업로드 성공", null);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 특정 배경화면에 속한 모든 스티커 가져오기
    @GetMapping("/background/{backgroundId}")
    public ResponseEntity<CommonResponse> getStickersByBackgroundId(@PathVariable Long backgroundId) {
        List<StickerDTO> stickers = stickerService.findAllByBackgroundId(backgroundId);
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "스티커 가져오기 성공", stickers);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 사용자가 가진 스티커 가져오기
    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse> getStickersByUserId(@PathVariable String userId) {
        List<Long> stickerIds = stickerService.findStickerIdsByUserId(userId);
        CommonResponse res = new CommonResponse(200, HttpStatus.OK, "사용자 스티커 가져오기 성공", stickerIds);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    // 사용자의 현재 사용중인 스티커 불러오기
    @GetMapping("/user/current/{userId}")
    public ResponseEntity<CommonResponse> getCurrentStickers(@PathVariable String userId) {
        try {
            List<Sticker> currentStickers = stickerService.getCurrentStickers(userId);
            System.out.println("getCurrentStickers 호출됨: " + currentStickers);
            if (!currentStickers.isEmpty()) {
                List<StickerDTO> stickerDTOs = currentStickers.stream().map(sticker -> {
                    String imageBase64 = Base64.getEncoder().encodeToString(sticker.getImage());
                    return new StickerDTO(sticker.getId(), sticker.getName(), imageBase64, sticker.getBackground().getId(),sticker.getPos_left(), sticker.getPos_top());
                }).collect(Collectors.toList());
                CommonResponse res = new CommonResponse(200, HttpStatus.OK, "현재 스티커 가져오기 성공", stickerDTOs);
                return new ResponseEntity<>(res, res.getHttpStatus());
            } else {
                CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, "설정되어있는 스티커가 없습니다.", null);
                return new ResponseEntity<>(res, res.getHttpStatus());
            }
        } catch (IllegalArgumentException e) {
            CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (Exception e) {
            e.printStackTrace(); // 예외 스택 트레이스를 출력하여 디버깅 정보를 제공합니다.
            CommonResponse res = new CommonResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    // 특정 사용자가 현재 설정한 스티커 업데이트
    @PostMapping("/user/current")
    public ResponseEntity<CommonResponse> setCurrentStickers(@RequestBody SetCurrentStickersRequest request) {
        try {
            stickerService.setCurrentStickers(request.getUserId(), request.getStickerIds());
            CommonResponse res = new CommonResponse(200, HttpStatus.OK, "현재 스티커 설정 성공", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (IllegalArgumentException e) {
            CommonResponse res = new CommonResponse(404, HttpStatus.NOT_FOUND, e.getMessage(), null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        } catch (Exception e) {
            CommonResponse res = new CommonResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, "알 수 없는 오류가 발생했습니다.", null);
            return new ResponseEntity<>(res, res.getHttpStatus());
        }
    }

    // 내부 클래스: SetCurrentStickersRequest
    public static class SetCurrentStickersRequest {
        private String userId;
        private List<Long> stickerIds;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public List<Long> getStickerIds() {
            return stickerIds;
        }

        public void setStickerIds(List<Long> stickerIds) {
            this.stickerIds = stickerIds;
        }
    }
}
