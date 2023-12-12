package com.example.DummyTalk.User.Controller;

import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Jwt.TokenProvider;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Service.AuthSerivce;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthSerivce authSerivce;

    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(UserDTO userDTO){

        log.info("테스트입니다");

        return null;
    }


}
