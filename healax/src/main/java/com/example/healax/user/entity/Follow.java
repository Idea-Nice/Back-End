package com.example.healax.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;  // 날 팔로우한 사람

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;    // 내가 팔로우 하고 있는 사람

}
