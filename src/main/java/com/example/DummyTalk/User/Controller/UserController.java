package com.example.DummyTalk.User.Controller;

import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.User.DTO.FriendDTO;
import com.example.DummyTalk.User.DTO.TokenDTO;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {


    HttpTransport transport = new NetHttpTransport();

    private final UserService userService;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDTO> signUp(@RequestBody UserDTO user){

        log.info("user=====>{}", user);
        try{
            UserDTO result = userService.signUp(user);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ResponseDTO(HttpStatus.CREATED, "회원가입 성공", result));

        } catch (Exception e){

            UserDTO empty = new UserDTO();

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), empty));
        }
    }

    @PostMapping("/googleLogin")
    public ResponseEntity<ResponseDTO> googleLogin(@RequestBody Map<String, String> credential) throws Exception {

        String idTokenString = credential.get("credential");

        TokenDTO result = userService.googleLogin(idTokenString);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK, "구글 로그인에 성공하셨습니다.", result));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseDTO> findByUser(@PathVariable String userId){

        UserDTO result = userService.findByUser(userId);
        log.error("result=====>{}", result);
        
        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK, "유저 조회에 성공하였습니다.", result));
    }

    @PostMapping("/friend/{userId}")
    public ResponseEntity<ResponseDTO> findByUser(@PathVariable String userId,
                                                  @RequestBody Map<String, String> email){

        log.info("TEST");

        try{
            FriendDTO result = userService.saveFriend(userId, email);

            return  ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK, "친구 추가에 성공하셨습니다.", result));

        } catch (RuntimeException e){
            return  ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null));
        }
    }
}
