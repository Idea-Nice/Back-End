package com.example.healax.user.entity;

import com.example.healax.asmr.entity.UserAsmr;
import com.example.healax.background.entity.Background;
import com.example.healax.bookmark.entity.Bookmark;
import com.example.healax.character.entity.Character;
import com.example.healax.sticker.entity.Sticker;
import com.example.healax.user.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String userId;

    @Column(nullable = false, length = 100)
    private String userPw;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false)
    private int level = 1;

    @Column(nullable = false)
    private int exp = 1;

    @ColumnDefault("0")
    @Column(nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAsmr> userAsmrs;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following;

    public static User toSaveUserEntity(UserDTO userDTO) {

        User userEntity = new User();

        userEntity.userId = userDTO.getUserId();
        userEntity.userPw = userDTO.getUserPw();
        userEntity.userName = userDTO.getUserName();

        return userEntity;
    }

    @ManyToMany
    @JoinTable(
            name = "user_background",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "background_id")
    )
    private List<Background> backgrounds = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "current_background_id")
    private Background currentBackground;

    @ManyToMany
    @JoinTable(
            name = "user_character",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private List<Character> characters;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bookmark> bookmarks;

    @ManyToMany
    @JoinTable(
            name = "user_sticker",
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<Sticker> stickers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "current_user_sticker",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sticker_id")
    )
    private List<Sticker> currentStickers = new ArrayList<>();
}
