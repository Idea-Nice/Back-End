package com.example.healax.background.domain;

import com.example.healax.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Background {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 배경화면 이름
    @Column(nullable = false)
    private String name;

    // 이미지 퍼블릭 url ( 브라우저에 바로 이미지를 볼 수 있는 url)
    private String url;

    // 관계의 주인은 User엔티티, 보유관계를 나타내는 user_background 테이블을 관리하는 ownedBackground필드에 의해서 관리된다.
    @ManyToMany(mappedBy = "ownedBackgrounds")
    private Set<User> usersWithAccess = new HashSet<>();

}
