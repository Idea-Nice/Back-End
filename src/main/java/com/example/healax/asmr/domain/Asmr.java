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
    private String fileName;

    @Column(nullable = false)
    private String url;

    private String contentType;

    // 관계의 주인은 User엔티티, 보유관계를 나타내는 user_asmr 테이블을 관리하는 ownedAsmr필드에 의해서 관리된다.
    @ManyToMany(mappedBy = "ownedAsmr")
    private Set<User> usersWithAccess = new HashSet<>();
}
