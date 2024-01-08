package com.example.DummyTalk.Aws;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

    private final S3Client s3Client;
    private final Environment env;

    @Value("${chatAbsolutePath.dir}")
    private String chatAbsolutePath;

    /***
     * 이미지 업로드
     * @param file
     * @param BUCKET_DIR : 업로드할 버킷 디렉토리 (ex. channel-1/, profile/)
     * @return imageDto
     * @throws IOException
     */
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

            return new ImageDto(url, fileName, BUCKET_DIR+file.getOriginalFilename(), contentType);
        }
    }


    public byte[] getObjectBytes (String keyName) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(env.getProperty("cloud.s3.bucket"))
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            return objectBytes.asByteArray();

        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            log.error("AwsS3Service getObjectBytes error : " + e.getMessage());
        }

        return null;
    }

    public void deleteObject(String keyName) {
        try {
            log.info("\nAwsS3Service deleteObject : " + keyName);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(env.getProperty("cloud.s3.bucket"))
                    .key(keyName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            log.error("AwsS3Service deleteObject error : " + e.getMessage());
        }
    }
}
