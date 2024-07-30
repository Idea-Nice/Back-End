package com.example.healax.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @Column
    private String oauth;

    @Column(nullable = false)
    private int level = 0;

    @Column(nullable = false)
    private int exp = 0;

    @ColumnDefault("1")
    @Column(nullable = false)
    private boolean status;

    @Column
    private String profileMusic;

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following;


}
