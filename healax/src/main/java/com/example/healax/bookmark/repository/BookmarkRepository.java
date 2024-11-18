package com.example.healax.bookmark.repository;

import com.example.healax.bookmark.entity.Bookmark;
import com.example.healax.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUser(User user);
}
