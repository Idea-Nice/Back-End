package com.example.healax.background.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.dto.UserBackgroundDTO;
import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackgroundService {
    private final BackgroundRepository backgroundRepository;
    private final UserRepository userRepository;

    public List findAll(){
        List<Background> backgrounds = backgroundRepository.findAll();

        List<BackgroundDTO> backgroundDTOs = backgrounds.stream().map(background -> {
            String imageBase64 = Base64.getEncoder().encodeToString(background.getImage());
            return new BackgroundDTO(background.getId(), background.getName(), imageBase64);
        }).collect(Collectors.toList());

        return backgroundDTOs;
    }

    public List<Long> findBackgroundIdsByUserId(String userId) {
        return backgroundRepository.findBackgroundIdsByUserId(userId);
    }

    public void addBackgroundToUser(UserBackgroundDTO userBackgroundDTO){
        Optional<User> userOptional = userRepository.findByUserId(userBackgroundDTO.getUserId());
        Optional<Background> backgroundOptional = backgroundRepository.findById(userBackgroundDTO.getBackId());

        if (userOptional.isPresent() && backgroundOptional.isPresent()) {
            User user = userOptional.get();
            Background background = backgroundOptional.get();

            user.getBackgrounds().add(background);
            userRepository.save(user);
        }
    }
}
