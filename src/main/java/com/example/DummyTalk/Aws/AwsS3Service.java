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
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.*;
import java.net.URL;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

    private final S3Client s3Client;
    private final Environment env;

    @Value("${chatAbsolutePath.dir}")
    private String chatAbsolutePath;

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


    public byte[] getObjectBytes (String keyName) {

        try {
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(env.getProperty("cloud.s3.bucket"))
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();

            File myFile = new File(chatAbsolutePath);
            OutputStream os = new FileOutputStream(myFile);
            os.write(data);
            log.info("\nAwsS3Service getObjectBytes success : " + myFile.getAbsolutePath());
            os.close();

            return data;

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (S3Exception e) {
            log.error(e.awsErrorDetails().errorMessage());
            log.error("AwsS3Service getObjectBytes error : " + e.getMessage());
        }
        return null;
    }
}
