package com.team.heyyo.util.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.team.heyyo.util.s3.dto.S3ImageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class S3Service {
    @Autowired
    private AmazonS3Client amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3ImageDto saveFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String serverFileName = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, serverFileName, multipartFile.getInputStream(), metadata);
        return S3ImageDto.of(originalFileName, serverFileName);
    }

    public ResponseEntity<UrlResource> downloadImage(String serverFileName) {
        UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, serverFileName));

        String contentDisposition = "attachment; filename=\"" +  serverFileName + "\"";

        // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    public String getImageUrl(String serverFileName) {
        return amazonS3.getUrl(bucket, serverFileName).toString();
    }

    public void deleteImage(String serverFileName)  {
        amazonS3.deleteObject(bucket, serverFileName);
    }


}