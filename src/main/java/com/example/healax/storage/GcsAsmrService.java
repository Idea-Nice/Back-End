package com.example.healax.storage;

import com.example.healax.asmr.domain.Asmr;
import com.example.healax.asmr.repository.AsmrRepository;
import com.example.healax.exception.CustomException;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GcsAsmrService implements StorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
    private final AsmrRepository asmrRepository;

    @Value("${gcs.bucket.asmr}")
    private String bucketName;

    // Asmr 파일 버킷에 업로드
    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        if (fileName == null) {
            throw new IOException("유효하지 않은 파일명입니다.");
        }

        // GCS 업로드
        Bucket bucket = storage.get(bucketName);
        Blob blob = bucket.create(fileName, file.getInputStream(), file.getContentType());
        String mediaUrl = blob.getMediaLink();

        System.out.println("File Uploaded: " + fileName + " URL: " + mediaUrl); // 디버깅용 로그

        // DB 저장
        Asmr asmr = new Asmr();
        asmr.setFileName(fileName);
        asmr.setUrl(mediaUrl);
        asmr.setContentType(file.getContentType());
        asmrRepository.save(asmr);

        return fileName;
    }

    // 파일 이름으로 url 꺼내오기
    public String getAsmrUrl(String fileName) {
        Optional<Asmr> asmrOptional = asmrRepository.findByFileName(fileName);
        if (asmrOptional.isEmpty()) {
            throw new CustomException("해당 이름의 음원파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        }

        Asmr asmr = asmrOptional.get();
        return asmr.getUrl();
    }

}
