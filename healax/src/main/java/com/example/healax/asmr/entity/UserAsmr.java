package com.example.healax.asmr.entity;

import com.example.healax.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserAsmr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "asmr_audio_file_id", nullable = false)
    private Asmr asmr;

}
