package com.example.healax.playlist.controller;

import com.example.healax.exception.CommonResponse;
import com.example.healax.playlist.domain.Playlist;
import com.example.healax.playlist.dto.AddVideoRequest;
import com.example.healax.playlist.dto.AddVideosByKeywordOrGenreRequest;
import com.example.healax.playlist.dto.RemoveVideoRequest;
import com.example.healax.playlist.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<CommonResponse> addVideoByUrl(@RequestBody AddVideoRequest request) {
        playlistService.addVideoByUrl(request.getUserId(), request.getVideoUrl());
        CommonResponse response = new CommonResponse(
                "입력된 영상 url을 통해 해당 영상을 정상적으로 재생목록에 추가했습니다.",
                201
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);  // http status code 201 : 성공적으로 creation이 완료되었을 때 사용
    }

    // ai에 프롬프팅해서 영상 3개 추가하기
    @PostMapping("/add-ai")
    public ResponseEntity<CommonResponse> addVideosFromAi(@RequestBody AddVideosByKeywordOrGenreRequest request) {
        int addedCount = playlistService.addVideosFromAi(request.getUserId(), request.getKeywordOrGenre());
        CommonResponse response = new CommonResponse(
                "정상적으로 요청하신 내용을 분석해 정상적으로 곡을 재생목록에 추가했습니다. 추가한 곡 수 : " + addedCount,
                201
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 장르 (lofi, piano 등) 클릭해서 영상 10개 추가하기
    @PostMapping("/add-genre")
    public ResponseEntity<CommonResponse> addVideosByGenre(@RequestBody AddVideosByKeywordOrGenreRequest request) {
        int addedCount = playlistService.addVideosFromGenre(request.getUserId(), request.getKeywordOrGenre());
        CommonResponse response = new CommonResponse(
                "정상적으로 해당 장르의 곡들을 재생목록에 추가했습니다. 추가한 곡 수 : " + addedCount,
                201
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    // 재생목록에서 영상 삭제하기
    @DeleteMapping("/remove")
    public ResponseEntity<CommonResponse> removeVideoFromPlaylist(@RequestBody RemoveVideoRequest requestDTO) {
        playlistService.removeVideoFromPlaylist(requestDTO);
        CommonResponse response = new CommonResponse(
                "정상적으로 해당 곡을 제거했습니다.",
                204
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);  // http status code 204 : 성공적으로 삭제 요청이 처리되었을 때 사용
    }
}
