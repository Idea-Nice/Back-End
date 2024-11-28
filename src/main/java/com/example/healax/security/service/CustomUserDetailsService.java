package com.example.healax.security.service;

import com.example.healax.security.dto.UserContext;
import com.example.healax.user.dto.UserDTO;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* 사용자의 정보를 DB 에서 가져오는 서비스 */
@Service("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByUserId(username);

        if (user.isEmpty()) {

            throw new UsernameNotFoundException(username + " 해당 아이디를 찾을 수 없습니다.");
        }

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.get().getRoles()));

        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.get().getId());
        userDTO.setUserId(user.get().getUserId());
        userDTO.setUserPw(user.get().getUserPw());
        userDTO.setUserName(user.get().getUserName());
        userDTO.setRoles(user.get().getRoles());

        return new UserContext(userDTO, authorities);
    }
}
