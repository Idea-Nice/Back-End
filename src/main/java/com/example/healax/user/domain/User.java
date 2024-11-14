package com.example.healax.user.domain;

import com.example.healax.background.domain.Background;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
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

    @Column(nullable = false, length = 30, unique = true)
    private String userId;

    @Column(nullable = false, length = 30, unique = true)
    private String userPw;

    @Column(nullable = false, length = 30, unique = true)
    private String userName;

    @ManyToOne
    @JoinColumn(name = "current_background_id")
    private Background currentBackground;

    @ManyToMany
    @JoinTable(
            name = "user_background",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "background_id")
    )
    private Set<Background> ownedBackgrounds = new HashSet<>(); // 중복 생성이 방지되도록 List 대신 Set으로 담는다.



    // background 요소 추가할 때 호출할 메서드 (set이므로 add()가 필요함)
    public void addOwnedBackground(Background background) {
        ownedBackgrounds.add(background);
    }

    // currentBackground setter
    public void setCurrentBackground(Background background) {
        this.currentBackground = background;
    }
}
