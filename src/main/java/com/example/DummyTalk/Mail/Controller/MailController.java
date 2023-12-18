package com.example.DummyTalk.Mail.Controller;

import com.example.DummyTalk.Mail.Service.MailService;
import com.example.DummyTalk.User.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/userEmail/{userId}")
    public String authMail(@RequestBody String userEmail, @PathVariable String userId){

        int number = mailService.sendMail(userEmail);

        return  String.valueOf(number);

    }

}
