package com.example.DummyTalk.User.Controller;

import com.example.DummyTalk.Jwt.TokenProvider;
import com.example.DummyTalk.User.Service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authSerivce;

    private final TokenProvider tokenProvider;

}
