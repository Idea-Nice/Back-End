package com.example.healax.youtube.dto;

import com.example.healax.playlist.domain.Playlist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YoutubeDTO {
    private String videoId;
    private String title;
    private String thumbnailUrl;
    private String videoUrl;
    private String channelTitle;

    public Playlist toPlaylistEntity() {
        Playlist playlist = new Playlist();
        playlist.setVideoId(this.videoId);
        playlist.setTitle(this.title);
        playlist.setThumbnailUrl(this.thumbnailUrl);
        playlist.setVideoUrl(this.videoUrl);
        playlist.setChannelTitle(this.channelTitle);
        return playlist;
    }
}
