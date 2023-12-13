package com.example.DummyTalk.User.Service;

import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDTO signUp(UserDTO userDTO) {

        String email = userDTO.getEmail();

        boolean isExists =  userRepository.existsByUserEmail(email);


        if (!isExists){
            User user = modelMapper.map(userDTO, User.class);

            User createUser= userRepository.save(user);

            return modelMapper.map(createUser, UserDTO.class);
        } else {

            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

    }
}
