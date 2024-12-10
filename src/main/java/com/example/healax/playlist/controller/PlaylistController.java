package com.example.healax.playlist.controller;

import com.example.healax.playlist.domain.Playlist;
import com.example.healax.playlist.dto.AddVideoRequest;
import com.example.healax.playlist.dto.AddVideosByKeywordOrGenreRequest;
import com.example.healax.playlist.dto.RemoveVideoRequest;
import com.example.healax.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // 재생목록에 url로 영상 추가하기
    @PostMapping("/add-url")
    public ResponseEntity<Void> addVideoByUrl(@RequestBody AddVideoRequest request) {
        playlistService.addVideoByUrl(request.getUserId(), request.getVideoUrl());
        return ResponseEntity.status(201).build();  // http status code 201 : 성공적으로 creation이 완료되었을 때 사용
    }

    // ai에 프롬프팅해서 영상 3개 추가하기
    @PostMapping("/add-ai")
    public ResponseEntity<String> addVideosFromAi(@RequestBody AddVideosByKeywordOrGenreRequest request) {
        int addedCount = playlistService.addVideosFromAi(request.getUserId(), request.getKeywordOrGenre());
        return ResponseEntity.status(201).body(addedCount + "개의 동영상이 추가되었습니다.");
    }

    // 장르 (lofi, piano 등) 클릭해서 영상 10개 추가하기
    @PostMapping("/add-genre")
    public ResponseEntity<String> addVideosByGenre(@RequestBody AddVideosByKeywordOrGenreRequest request) {
        int addedCount = playlistService.addVideosFromGenre(request.getUserId(), request.getKeywordOrGenre());
        return ResponseEntity.status(201).body(addedCount + "개의 동영상이 추가되었습니다.");
    }


    // 재생목록에서 영상 삭제하기
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeVideoFromPlaylist(@RequestBody RemoveVideoRequest requestDTO) {
        playlistService.removeVideoFromPlaylist(requestDTO);
        return ResponseEntity.noContent().build();  // http status code 204 : 성공적으로 삭제 요청이 처리되었을 때 사용
    }
}
