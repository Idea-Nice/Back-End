package com.example.healax.storage;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
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
public class GcsBackgroundService implements StorageService{
    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    @Value("${gcs.bucket.background}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        String uniqueFileName = "background/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uniqueFileName)
                .setAcl(java.util.Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
                .build();

        storage.create(blobInfo, file.getBytes());

        return getPublicUrl(uniqueFileName);
    }

    private String getPublicUrl(String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
}
