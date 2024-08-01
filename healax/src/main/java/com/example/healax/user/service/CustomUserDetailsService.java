//package com.example.healax.user.service;
//
//
//import com.example.healax.user.dto.CustomUserDetailsDTO;
//import com.example.healax.user.entity.User;
//import com.example.healax.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//
//        Optional<User> userOptional = userRepository.findByUserId(userId);
//
//        System.out.println("userOptional: " + userOptional);
//
//        if (userOptional.isPresent()) {
//
//            User user = userOptional.get();
//
//            return new CustomUserDetailsDTO(user);
//        }
//
//        return null;
//    }
//
//}
