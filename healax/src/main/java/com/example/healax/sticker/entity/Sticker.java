package com.example.healax.sticker.entity;

import com.example.healax.background.entity.Background;
import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Sticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;

    // 좌표정보
    private int pos_left;
    private int pos_top;

    @ManyToOne
    @JoinColumn(name = "background_id")
    private Background background;

    @ManyToMany(mappedBy = "stickers")
    private List<User> users;
}
