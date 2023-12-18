package com.example.DummyTalk.Mail.Controller;

import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.Mail.Service.MailService;
import com.example.DummyTalk.User.DTO.UserDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/userEmail")
    public ResponseEntity<ResponseDTO> authMail(@RequestBody String userEmail){

        int number = mailService.sendMail(userEmail);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDTO(HttpStatus.OK, "발급 성공", null));
    }

}
