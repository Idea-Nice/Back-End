package com.example.healax.bell.controller;

import com.example.healax.bell.entity.Bell;
import com.example.healax.bell.service.BellService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/bell")
public class BellController {

    private final BellService bellService;

    public BellController(BellService bellService) {
        this.bellService = bellService;
    }

    // 해당 유저 알람 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<Bell>> getBellsByUserId(@PathVariable String userId) {
        List<Bell> bells = bellService.getBellsByUserId(userId);
        return ResponseEntity.ok(bells);
    }

    // 신규 알람 저장하기
    @PostMapping("/save/{userId}")
    public ResponseEntity<Bell> saveBell(@RequestBody Bell bell, @PathVariable String userId) {
        Bell saveBell = bellService.saveBell(bell, userId);
        return ResponseEntity.ok(saveBell);
    }

    // 알람 수정하기
    @PostMapping("/modify/{userId}")
    public ResponseEntity<Bell> modifyBell(@RequestBody Bell bell, @PathVariable String userId) {
        Bell modifiedBell = bellService.modifyBell(bell, userId);
        return ResponseEntity.ok(modifiedBell);
    }

    // 알람 삭제
    @DeleteMapping("/delete/{bellId}")
    public ResponseEntity<Void> deleteBell(@PathVariable Long bellId) {
        bellService.deleteBell(bellId);
        return ResponseEntity.noContent().build();
    }

    // 알람 여러개 한번에 삭제
    @DeleteMapping("/deleteMany")
    public ResponseEntity<Void> deleteManyBells(@RequestBody List<Long> bellIds) {
        bellIds.forEach(bellService::deleteBell);
        return ResponseEntity.noContent().build();
    }
}
