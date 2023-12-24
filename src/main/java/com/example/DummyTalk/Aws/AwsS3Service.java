package com.example.DummyTalk.Aws;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

    private final S3Client s3Client;
    private final Environment env;

    public ImageDto upload(MultipartFile file, String BUCKET_DIR) throws IOException {
        try (InputStream fileStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(env.getProperty("cloud.s3.bucket"))
                    .key(BUCKET_DIR + fileName)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(fileStream, file.getSize()));
            log.info("\nAwsS3Service response : " + response.toString());

            String url = env.getProperty("cloud.s3.url.path") + BUCKET_DIR + fileName;

            return new ImageDto(url, fileName, file.getOriginalFilename() + file.getContentType());
        }
    }
}
