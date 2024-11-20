package com.example.healax.user.domain;

import com.example.healax.asmr.domain.Asmr;
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
    @JoinColumn(name = "current_background_id", foreignKey = @ForeignKey(name = "fk_user_background_user"))
    private Background currentBackground;

    // 회원이 보유한 Background
    @ManyToMany
    @JoinTable(
            name = "user_background",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_background_user")),
            inverseJoinColumns = @JoinColumn(name = "background_id", foreignKey = @ForeignKey(name = "fk_user_background_user"))
    )
    private Set<Background> ownedBackgrounds = new HashSet<>(); // 중복 생성이 방지되도록 List 대신 Set으로 담는다.

    //회원이 보유한 Asmr
    @ManyToMany
    @JoinTable(
            name = "user_asmr",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_asmr_user")),
            inverseJoinColumns = @JoinColumn(name = "asmr_id", foreignKey = @ForeignKey(name = "fk_asmr_user"))
    )
    private Set<Asmr> ownedAsmr = new HashSet<>();

    // background 요소 추가할 때 호출할 메서드 (set이므로 add()가 필요함)
    public void addOwnedBackground(Background background) {
        ownedBackgrounds.add(background);
    }

    // asmr 요소 추가할 때 호출할 메서드 (set이므로 add()가 필요함)
    public void addOwnedAsmr(Asmr asmr) {
        ownedAsmr.add(asmr);
    }

}
