//package com.example.healax.user.dto;
//
//import com.example.healax.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.List;
//
//@RequiredArgsConstructor
//public class CustomUserDetailsDTO implements UserDetails {
//
//    private final User user;
//
//    // 권한 목록
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of();
//    }
//
//    @Override
//    public String getPassword() {
//        return user.getUserPw();
//    }
//
//    @Override
//    public String getUsername() {
//        return user.getUserId();
//    }
//
//    // 계정 만료 여부
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    // 계정 잠김 여부
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    // 비밀번호 만료 여부
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    // 사용자 활성화 여부
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//}
