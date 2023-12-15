package com.example.DummyTalk.User.Service;

import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserDTO signUp(UserDTO userDTO) {



        String email = userDTO.getUserEmail();

        boolean isExists =  userRepository.existsByUserEmail(email);



        if (!isExists){

            LocalDateTime currentDateTime = LocalDateTime.now();
            // 서울시간으로 가져오기 위해 + 9시간
            LocalDateTime plus9Hours = currentDateTime.plusHours(9);

            // 닉네임을 입력하지 않았을 경우 이름으로 등록
            if(userDTO.getNickname().isEmpty()){
                userDTO.setNickname(userDTO.getName());
            }
            
            // 비밀번호 인코딩
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            
            // 현재 시간 추가
            userDTO.setCreateAt(plus9Hours);

            User user = modelMapper.map(userDTO, User.class);

            User createUser= userRepository.save(user);

            return modelMapper.map(createUser, UserDTO.class);
        } else {

            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

    }
}
