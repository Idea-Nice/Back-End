package com.example.healax.storage;

import com.example.healax.asmr.repository.AsmrRepository;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GcsAsmrService implements StorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final AsmrRepository asmrRepository;

    @Value("${gcs.bucket.asmr}")
    private String bucketName;

    @Value("${gcs.directory.asmr.audio}")
    private String audioDirectory;

    @Value("${gcs.directory.asmr.image}")
    private String imageDirectory;

    // Asmr 파일 업로드 - audio/image 여부를 전달해 해당 디렉토리에 저장
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        throw new UnsupportedOperationException("uploadFile 호출 시 디렉토리가 명시되어야 합니다.");
    }

    // 음원 업로드일 경우
    public String uploadAudio(MultipartFile file) throws IOException {
        return uploadFileToDirectory(file, audioDirectory);
    }

    // 이미지 업로드일 경우
    public String uploadImage(MultipartFile file) throws IOException {
        return uploadFileToDirectory(file, imageDirectory);
    }

    // 내부적으로 디렉토리를 명시해서 파일 업로드
    private String uploadFileToDirectory(MultipartFile file, String subDirectory) throws IOException {
        String originalFileName = file.getOriginalFilename();

        if(originalFileName == null) {
            throw new IOException("유효하지 않은 파일입니다.");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
        String filePath = subDirectory + "/" + uniqueFileName;

        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.create(filePath, file.getInputStream(), file.getContentType());
        return blob.getMediaLink();
    }

}
