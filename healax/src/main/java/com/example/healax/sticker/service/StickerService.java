package com.example.healax.sticker.service;

import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import com.example.healax.sticker.dto.StickerDTO;
import com.example.healax.sticker.dto.UserStickerDTO;
import com.example.healax.sticker.entity.Sticker;
import com.example.healax.sticker.repository.StickerRepository;
import com.example.healax.user.entity.User;
import com.example.healax.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StickerService {
    private final StickerRepository stickerRepository;
    private final UserRepository userRepository;
    private final BackgroundRepository backgroundRepository;

    public List<StickerDTO> findAllByBackgroundId(Long backgroundId) {
        List<Sticker> stickers = stickerRepository.findByBackgroundId(backgroundId);
        return stickers.stream().map(sticker -> {
            String imageBase64 = Base64.getEncoder().encodeToString(sticker.getImage());
            return new StickerDTO(sticker.getId(), sticker.getName(), imageBase64, sticker.getBackground().getId(),sticker.getLeft(), sticker.getTop());
        }).collect(Collectors.toList());
    }

    @Transactional
    public void addStickerToUser(UserStickerDTO userStickerDTO) {
        Optional<User> userOptional = userRepository.findByUserId(userStickerDTO.getUserId());
        Optional<Sticker> stickerOptional = stickerRepository.findById(userStickerDTO.getStickerId());

        if (userOptional.isPresent() && stickerOptional.isPresent()) {
            User user = userOptional.get();
            Sticker sticker = stickerOptional.get();

            if (user.getBackgrounds().contains(sticker.getBackground())) {
                user.getStickers().add(sticker);
                userRepository.save(user);
            }
        }
    }

    @Transactional
    public void grantStickerAccessByLevel(User user) {
        int level = user.getLevel();
        for (long stickerIdToAdd = 1; stickerIdToAdd <= level; stickerIdToAdd++) {
            Optional<Sticker> stickerOptional = stickerRepository.findById(stickerIdToAdd);
            if (stickerOptional.isPresent()) {
                Sticker sticker = stickerOptional.get();
                if (user.getBackgrounds().contains(sticker.getBackground()) && !user.getStickers().contains(sticker)) {
                    user.getStickers().add(sticker);
                }
            }
        }
        userRepository.save(user);
    }

    @Transactional
    public void saveSticker(String name, MultipartFile image, Long backgroundId, int left, int top) throws IOException {
        Optional<Background> backgroundOptional = backgroundRepository.findById(backgroundId);

        if (backgroundOptional.isPresent()) {
            Sticker sticker = new Sticker();
            sticker.setName(name);
            sticker.setImage(image.getBytes());
            sticker.setBackground(backgroundOptional.get());
            sticker.setLeft(left);
            sticker.setTop(top);
            stickerRepository.save(sticker);
        } else {
            throw new IllegalArgumentException("해당 배경화면을 찾을 수 없습니다.");
        }
    }

    public List<Long> findStickerIdsByUserId(String userId) {
        return stickerRepository.findStickerIdsByUserId(userId);
    }

    // 현재 사용중 스티커 저장하기
    @Transactional
    public void setCurrentStickers(String userId, List<Long> stickerIds) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Sticker> stickers = stickerRepository.findAllById(stickerIds);

            for (Sticker sticker : stickers) {
                if (!user.getStickers().contains(sticker)) {
                    throw new IllegalArgumentException("해당 스티커에 대한 접근 권한이 없습니다.");
                }
            }

            user.setCurrentStickers(stickers);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
    }

    // 현재 사용중 스티커 불러오기
    @Transactional
    public List<Sticker> getCurrentStickers(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Sticker> currentStickers = user.getCurrentStickers();
            System.out.println("현재 스티커: " + currentStickers);
            if (currentStickers != null) {
                return currentStickers;
            } else {
                return new ArrayList<>(); // null일 경우 빈 리스트 반환
            }
        } else {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
    }
}
