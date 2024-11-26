package com.example.healax.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 재생목록 영상 추가용 요청 DTO
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddVideoRequest {
    private String userId;
    private String videoUrl;    // 추가할 영상의 재생페이지 url을 가져온다
}
