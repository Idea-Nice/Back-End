package com.example.healax.user.domain;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.background.domain.Background;
import com.example.healax.playlist.domain.Playlist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 100, unique = true)
    private String userId;

    @Column(nullable = false, length = 100, unique = true)
    private String userPw;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false, length = 30)
    private String roles;

    // 현재 설정된 배경화면
    @ManyToOne
    @JoinColumn(
            name = "current_background_id",
            foreignKey = @ForeignKey(name = "fk_user_background_current")
    )
    private Background currentBackground;

    // 회원이 보유한 Background
    @ManyToMany
    @JoinTable(
            name = "user_background",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_background_owned_user")),
            inverseJoinColumns = @JoinColumn(name = "background_id", foreignKey = @ForeignKey(name = "fk_user_background_owned_background"))
    )
    private Set<Background> ownedBackgrounds = new HashSet<>();

    // 회원이 보유한 Asmr
    @ManyToMany
    @JoinTable(
            name = "user_asmr",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_asmr_user")),
            inverseJoinColumns = @JoinColumn(name = "asmr_id", foreignKey = @ForeignKey(name = "fk_user_asmr_asmr"))
    )
    private Set<Asmr> ownedAsmr = new HashSet<>();


    // 유저 개인 재생 목록 관리용 컬럼
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<Playlist> playlist = new ArrayList<>();

    // background 요소 추가할 때 호출할 메서드 (set이므로 add()가 필요함)
    public void addOwnedBackground(Background background) {
        ownedBackgrounds.add(background);
    }

    // asmr 요소 추가할 때 호출할 메서드 (set이므로 add()가 필요함)
    public void addOwnedAsmr(Asmr asmr) {
        ownedAsmr.add(asmr);
    }

    // 기본 배경화면 설정할 때 호출할 메서드
    public void setDefaultCurrentBackground(Background background) {
        this.currentBackground = background;
    }
}
