package com.example.healax.gcptest;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class StorageService {
    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public StorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadImage(MultipartFile file) throws IOException {
        // 파일명 생성 (UUID로 유일한 이름 설정)
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        // BLOB 정보 생성
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, fileName).build();

        // 파일을 GCS에 업로드
        storage.create(blobInfo, file.getBytes());

        // 파일의 GCS URL 반환
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
}
