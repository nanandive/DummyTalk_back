package com.example.DummyTalk.AES;

import lombok.Getter;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.Key;

public class AESUtil {

    private static SecretKey key;

    private static final String KEY_FILE_PATH = "aes_key.dat";
    protected static SecretKey getKey() {
        return loadKeyFromFile(KEY_FILE_PATH);
    }

    // 랜덤한 AES Key 생성 (128 bit key size)
    protected static SecretKey generateAESKey() throws Exception {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        key =  keyGenerator.generateKey();
        // 파일에 키를 저장
        saveKeyToFile(KEY_FILE_PATH, key);
        return key;
    }

    // 암호화
    protected static byte[] encrypt(String message, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(message.getBytes());
    }

    // 복호화
    protected static String decrypt(byte[] encryptedBytes, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }


    private static void saveKeyToFile(String filePath, SecretKey key) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static SecretKey loadKeyFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (SecretKey) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}
