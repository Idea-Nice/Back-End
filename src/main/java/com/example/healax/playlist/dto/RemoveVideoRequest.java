package com.example.healax.playlist.dto;

// 재생목록에서 영상 제거용 요청 DTO

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RemoveVideoRequest {
    private String userId;
    private String videoId;
}
