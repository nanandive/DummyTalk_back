package com.example.DummyTalk.Aws;
import

        lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.s3.S3Client;


@RequiredArgsConstructor
@Configuration
public class AwsConfig {

    private final Environment env;

    @Bean
    public S3Client amazonS3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(env.getProperty("cloud.s3.access-id"), env.getProperty("cloud.s3.secret-key"));

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public KmsClient amazonKmsClient() {

        AwsBasicCredentials credentials = AwsBasicCredentials.create(env.getProperty("kms.access-id"), env.getProperty("kms.secret-key"));

        return KmsClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

    }
}
