//package com.example.DummyTalk.Mail.Controller;
//
//import com.example.DummyTalk.Mail.Service.MailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//public class MailController {
//
//    private final MailService mailService;
//
//    @GetMapping("/authMail")
//    public String authMail(String mail){
//
//        int number = mailService.sendMail(mail);
//
//        return  String.valueOf(number);
//
//    }
//
//}
