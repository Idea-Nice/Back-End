package com.example.healax.user.controller;

import com.example.healax.user.service.UserLevelService;
import com.example.healax.config.LevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "http://43.203.68.91:80/", allowedHeaders = "*")
public class UserLevelController {

    private final UserLevelService userLevelStatusService;

    //회원 레벨 가져오기
    @GetMapping("/level-get/{user_id}")
    public ResponseEntity<LevelResponse> userLevel(@PathVariable String user_id) {
        Integer level = userLevelStatusService.getLevel(user_id);

        LevelResponse res = new LevelResponse(
                200,
                HttpStatus.OK,
                level,
                " 레벨 가져오기 성공",
                null
        );
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // 레벨에 따른 권한 새로고침 엔드포인트
    @PostMapping("/refresh-access/{userId}")
    public ResponseEntity<String> refreshAccess(@PathVariable("userId") String userId) {
        try{
            userLevelStatusService.refreshAccess(userId);
            return ResponseEntity.ok("접근 권한이 성공적으로 갱신되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다. : " + e);
        }
    }

    // 사용자 레벨 조정 + 권한 새로고침 엔드포인트
    @PostMapping("/adjust-level")
    public ResponseEntity<String> adjustUserLevel(@RequestBody AdjustUserLevelRequest request) {
        try {
            userLevelStatusService.adjustUserLevel(request.getUserId(), request.getNewLevel());
            return ResponseEntity.ok("사용자 레벨이 성공적으로 조정되고 권한이 갱신되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch(Exception e) {
            return ResponseEntity.status(500).body("알 수 없는 오류가 발생했습니다." + e.getMessage());
        }
    }

    //adjustUserLevel의 파라미터(리퀘스트바디)를 위한 DTO
    public static class AdjustUserLevelRequest {
        private String userId;
        private int newLevel;

        public String getUserId(){
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getNewLevel() {
            return newLevel;
        }

        public void setNewLevel(int newLevel) {
            this.newLevel = newLevel;
        }
    }
}
