package com.example.DummyTalk.AES;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.*;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.ByteBuffer;
import java.security.Key;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AESUtil {
    private  SecretKey key;

    private final Environment env;

    private static final String KEY_FILE_PATH = "aes_key.dat";


    // 랜덤한 AES Key 생성 (128 bit key size)
//    public static SecretKey generateAESKey() throws Exception {
//
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(128);
//        key =  keyGenerator.generateKey();
//        return key;
//    }

    // 암호화
    public String encrypt(KmsClient kmsClient, String plaintext) throws Exception {

        try {

            // 암호화할 데이터를 바이트 배열로 생성
            SdkBytes myBytes = SdkBytes.fromByteArray(plaintext.getBytes());

            // 암호화 요청 생성
            EncryptRequest encryptRequest = EncryptRequest.builder()
                    .keyId(env.getProperty("kms.key-id"))          // 암호화에 사용할 대칭키의 ID 지정
                    .plaintext(myBytes)                                     // 암호화할 데이터를 지정
                    .build();

            // AWS KMS를 사용하여 데이터 암호화 요청
            EncryptResponse response = kmsClient.encrypt(encryptRequest);

            // 암호화에 사용된 알고리즘 확인
            String algorithm = response.encryptionAlgorithm().toString();
            System.out.println("The encryption algorithm is " + algorithm);

            // 암호화된 데이터 획득
            SdkBytes encryptedData = response.ciphertextBlob();

            String result = Base64.getEncoder().encodeToString(encryptedData.asByteArray());

            return result;

        } catch (KmsException e) {
            // AWS KMS에서 예외가 발생한 경우 처리
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    // 복호화
    public String decrypt(KmsClient kmsClient, String ciphertext) throws Exception {

        // 암호화된 데이터 Base64 디코딩
        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);

        try {
            DecryptRequest decryptRequest = DecryptRequest.builder()
                    .ciphertextBlob(SdkBytes.fromByteArray(ciphertextBytes))
                    .keyId(env.getProperty("kms.key-id"))
                    .build();

            DecryptResponse decryptResponse = kmsClient.decrypt(decryptRequest);
            SdkBytes decryptedData = decryptResponse.plaintext();


            String result = Base64.getEncoder().encodeToString(decryptedData.asByteArray());

            return result;

        } catch (KmsException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }
}
