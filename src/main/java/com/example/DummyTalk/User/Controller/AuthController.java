package com.example.DummyTalk.User.Controller;

import com.example.DummyTalk.Jwt.TokenProvider;
import com.example.DummyTalk.User.Service.AuthSerivce;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthSerivce authSerivce;

    private final TokenProvider tokenProvider;

//    @GetMapping("/login")
//    public ResponseEntity<ResponseEntity> login()


}
