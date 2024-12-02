package com.example.healax.playlist.controller;

import com.example.healax.playlist.domain.Playlist;
import com.example.healax.playlist.dto.AddVideoRequest;
import com.example.healax.playlist.dto.RemoveVideoRequest;
import com.example.healax.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/playlist")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000/")
public class PlaylistController {

    private final PlaylistService playlistService;

    // 해당 유저 재생목록 가져오기
    @GetMapping("/{userId}")
    public ResponseEntity<List<Playlist>> getUserPlaylist(@PathVariable String userId) {
        return ResponseEntity.ok(playlistService.getUserPlaylist(userId));
    }

    // 재생목록에 영상 추가하기
    @PostMapping("/add")
    public ResponseEntity<Void> addVideoToPlaylist(@RequestBody AddVideoRequest requestDTO) throws IOException {
        playlistService.addVideoToPlaylist(requestDTO);
        return ResponseEntity.status(201).build();  // http status code 201 : 성공적으로 creation이 완료되었을 때 사용
    }

    // 재생목록에서 영상 삭제하기
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeVideoFromPlaylist(@RequestBody RemoveVideoRequest requestDTO) {
        playlistService.removeVideoFromPlaylist(requestDTO);
        return ResponseEntity.noContent().build();  // http status code 204 : 성공적으로 삭제 요청이 처리되었을 때 사용
    }


}
