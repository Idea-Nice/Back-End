package com.example.healax.bookmark.service;

import com.example.healax.bookmark.entity.Bookmark;
import com.example.healax.bookmark.repository.BookmarkRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YouTube youTube;

    @Value("${youtube.api.key}")
    private String apiKey;

    @Autowired
    @Qualifier("youtubeObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    public BookmarkService(@Qualifier("jacksonObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 새로운 찜 영상 추가
    public Bookmark saveBookmark(String videoId, String userId) throws IOException {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        YouTube.Videos.List request = youTube.videos()
                .list("snippet")
                .setId(videoId)
                .setKey(apiKey);

        VideoListResponse response = request.execute();
        if (response.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invalid videoId");
        }

        Video video = response.getItems().get(0);

        // JSON 변환
        JsonNode videoNode = objectMapper.valueToTree(video);
        JsonNode snippetNode = videoNode.get("snippet");

        if (snippetNode == null) {
            throw new IllegalArgumentException("Snippet not found");
        }

        Bookmark bookmark = new Bookmark();
        bookmark.setVideoId(videoId);
        bookmark.setTitle(snippetNode.get("title").asText());
        bookmark.setThumbnailUrl(snippetNode.get("thumbnails").get("default").get("url").asText());
        bookmark.setChannelId(snippetNode.get("channelId").asText());
        bookmark.setChannelTitle(snippetNode.get("channelTitle").asText());
        bookmark.setUser(user);

        return bookmarkRepository.save(bookmark);
    }

    // 해당 유저 찜목록 조회
    public List<Bookmark> getBookmarks(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOptional.get();

        return bookmarkRepository.findByUser(user);
    }

    // 해당 찜목록 삭제
    public void deleteBookmark(Long bookmarkId, String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if(userOptional.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }

        User user = userOptional.get();

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(bookmarkId);
        if(bookmarkOptional.isPresent()) {
            Bookmark bookmark = bookmarkOptional.get();
            if(bookmark.getUser().equals(user)) {
                bookmarkRepository.delete(bookmark);
            } else {
                throw new IllegalArgumentException("Bookmark does not belong to the user");
            }
        } else {
            throw new IllegalArgumentException("Bookmark not found");
        }
    }

}
