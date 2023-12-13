package com.example.DummyTalk.User.Controller;

import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody UserDTO user){


        UserDTO result = userService.signUp(user);

        try{
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", result));

        } catch (RuntimeException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "중복된 이메일이 존재합니다.", null));
        }
    }
}
