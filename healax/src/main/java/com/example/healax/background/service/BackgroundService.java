package com.example.healax.background.service;

import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import com.example.healax.background.dto.BackgroundDTO;
import com.example.healax.background.dto.UserBackgroundDTO;
import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BackgroundService {
    private final BackgroundRepository backgroundRepository;
    private final UserRepository userRepository;

    public List<BackgroundDTO> findAll(){
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

    @Transactional
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

    public void saveBackground(String name, MultipartFile image) throws IOException {
        Background background = new Background();
        background.setName(name);
        background.setImage(image.getBytes());
        backgroundRepository.save(background);
    }

    public Background findBackgroundById(Long id) {
        return backgroundRepository.findById(id).orElse(null);
    }

    @Transactional
    public void grantBackgroundAccessByLevel(User user) {
        int level = user.getLevel();

        for (long backgroundIdToAdd = 1; backgroundIdToAdd <= 6; backgroundIdToAdd++) {
            if (level >= (backgroundIdToAdd - 1) * 3 + 1) {
                Optional<Background> backgroundOptional = backgroundRepository.findById(backgroundIdToAdd);
                if (backgroundOptional.isPresent() && !user.getBackgrounds().contains(backgroundOptional.get())) {
                    user.getBackgrounds().add(backgroundOptional.get());
                }
            }
        }
        userRepository.save(user);
    }
}
