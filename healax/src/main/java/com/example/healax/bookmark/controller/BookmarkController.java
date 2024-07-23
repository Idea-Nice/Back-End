package com.example.healax.bookmark.controller;


import com.example.healax.bookmark.entity.Bookmark;
import com.example.healax.bookmark.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    // 찜 목록 생성하기
    @PostMapping("/create/{userId}")
    public Bookmark saveBookmark(@PathVariable String userId, @RequestParam String videoId) throws IOException {
        return bookmarkService.saveBookmark(videoId, userId);
    }

    // 해당 유저 북마크 목록 조회
    @GetMapping("/{userId}")
    public List<Bookmark> getBookmarks(@PathVariable String userId) {
        return bookmarkService.getBookmarks(userId);
    }

    // 찜 목록에서 삭제하기
    @DeleteMapping("/delete/{userId}/{bookmarkId}")
    public void deleteBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId, userId);
    }

}
