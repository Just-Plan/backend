package com.jyp.justplan.domain.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(Long folderId) {
        String key = "container/" + folderId + "_" + Instant.now().toEpochMilli();

        // folderId를 사용하여 파일 내용 생성
        String fileContent = "Folder ID: " + folderId;
        byte[] contentAsBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        ByteArrayInputStream contentStream = new ByteArrayInputStream(contentAsBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentAsBytes.length);
        metadata.setContentType("text/plain");

        amazonS3.putObject(bucket, key, contentStream, metadata);
        return amazonS3.getUrl(bucket, key).toString();
    }

    public String uploadProfilePicture(String userEmail, MultipartFile file) throws IOException {
        String key = "profiles/" + userEmail + "/profile_" + Instant.now().toEpochMilli() + ".jpg";

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3.putObject(bucket, key, file.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, key).toString();
    }

}
