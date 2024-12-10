package com.example.healax.playlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddVideosByKeywordOrGenreRequest {
    private String userId;
    private String keywordOrGenre; // 필요에 따라 장르가 될 수 있고, openAI의 키워드가 될 수 있음
}
