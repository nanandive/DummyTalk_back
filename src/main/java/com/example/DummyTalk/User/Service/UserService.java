package com.example.DummyTalk.User.Service;

import com.example.DummyTalk.AES.AESUtil;
import com.example.DummyTalk.Jwt.TokenProvider;
import com.example.DummyTalk.User.DTO.TokenDTO;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.zip.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService extends AESUtil {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    // 회원가입
    public UserDTO signUp(UserDTO userDTO) throws Exception {

        String email = userDTO.getUserEmail();

        boolean isExists =  userRepository.existsByUserEmail(email);

        if (!isExists){

            int keyLength = 64;

            // 안전한 랜덤 바이트 생성
            byte[] keyBytes = generateRandomBytes(keyLength);

            // Base64로 인코딩하여 JWT 시크릿 키 생성
            String jwtKey = Base64.getEncoder().encodeToString(keyBytes);

            // 랜덤한 AES키 생성
            SecretKey aesKey = AESUtil.generateAESKey();

            byte[] encrtptJWT = AESUtil.encrypt(jwtKey, aesKey);

            // 서울시간으로 가져오기 위해 + 9시간
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime plus9Hours = currentDateTime.plusHours(9);

            // 유저에게 현재 시간 추가
            userDTO.setCreateAt(plus9Hours);

            // 닉네임을 입력하지 않았을 경우 이름으로 등록
            if(userDTO.getNickname().isEmpty()){
                userDTO.setNickname(userDTO.getName());
            }

            // jwt secret key
            userDTO.setUserSecretKey(encrtptJWT);
            
            // 비밀번호 인코딩
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

            User user = modelMapper.map(userDTO, User.class);

            User createUser= userRepository.save(user);

            return modelMapper.map(createUser, UserDTO.class);
        } else {

            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

    }

    // 구글 로그인
    public TokenDTO googleLogin(String credential) throws Exception {

        User result =  userRepository.findByCredential(credential.substring(0, 500));

        // 등록된 계정이 아닐 경우 등록
        if(result == null){

            int keyLength = 64;

            // 안전한 랜덤 바이트 생성
            byte[] keyBytes = generateRandomBytes(keyLength);

            // Base64로 인코딩하여 JWT 시크릿 키 생성
            String jwtKey = Base64.getEncoder().encodeToString(keyBytes);

            // 랜덤한 AES키 생성
            SecretKey aesKey = AESUtil.generateAESKey();

            byte[] encrtptJWT = AESUtil.encrypt(jwtKey, aesKey);

            // 서울시간으로 가져오기 위해 + 9시간
            LocalDateTime currentDateTime = LocalDateTime.now();
            LocalDateTime plus9Hours = currentDateTime.plusHours(9);


            UserDTO userDTO = new UserDTO();
            userDTO.setNickname("기본 닉네임");
            userDTO.setCredential(credential.substring(0, 500));
            userDTO.setCreateAt(plus9Hours);
            userDTO.setUserSecretKey(encrtptJWT);

            User user = modelMapper.map(userDTO, User.class);

            User resultUser = userRepository.save(user);

            return tokenProvider.generateTokenDTO(resultUser);

        } else {

            return tokenProvider.generateTokenDTO(result);
        }


    }

    // 랜덤한 JWT SecretKey 생성
    private static byte[] generateRandomBytes(int length) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }


}
