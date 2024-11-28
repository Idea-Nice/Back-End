package com.example.healax.asmr.domain;

import com.example.healax.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Asmr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;    // 음원의 이름

    @Column(nullable = false)
    private String audioFileName;   // 음원 파일 이름

    @Column(nullable = false)
    private String audioUrl; // 음원 url

    private String audioContentType; // 음원 contentType

    @Column(nullable = false)
    private String imageFileName;   // 이미지 파일 이름

    @Column(nullable = false)
    private String imageUrl;    // 이미지 url

    private String imageContentType;    // 음원 contentType

    // 관계의 주인은 User엔티티, 보유관계를 나타내는 user_asmr 테이블을 관리하는 ownedAsmr필드에 의해서 관리된다.
    @ManyToMany(mappedBy = "ownedAsmr")
    private Set<User> usersWithAccess = new HashSet<>();
}
