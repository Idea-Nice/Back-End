package com.example.healax.config;

import com.example.healax.background.entity.Background;
import com.example.healax.background.repository.BackgroundRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Configuration
@AllArgsConstructor
public class BackgroundEntityInitializer implements ApplicationRunner {
    private static final String IMAGE_FILE_PATH = "static/image 41.jpg";

    private final BackgroundRepository backgroundRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 이미지 파일 경로
        Path imagePath = new ClassPathResource(IMAGE_FILE_PATH).getFile().toPath();

        // 이미지 파일 읽기
        byte[] imageBytes = Files.readAllBytes(imagePath);

        // 이미지 이름을 기준으로 데이터베이스에서 검색
        String imageName = "배경 1";
        List<Background> existingBackgrounds = backgroundRepository.findByName(imageName);

        if (existingBackgrounds.isEmpty()) {
        // Background 엔티티 생성 및 저장
        Background background = new Background();
        background.setName("배경 1");
        background.setImage(imageBytes);
        backgroundRepository.save(background);
        }
    }
}
