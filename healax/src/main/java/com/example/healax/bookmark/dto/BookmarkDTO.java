package com.example.healax.bookmark.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookmarkDTO {
    private Long id;
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String channelId;
    private String channelTitle;
    private String userId;
}
