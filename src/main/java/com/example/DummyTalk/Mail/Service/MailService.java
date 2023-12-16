//package com.example.DummyTalk.Mail.Service;
//
//
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class MailService {
//
//    private final JavaMailSender javaMailSender;
//
//    private static final String senderEmail= "wisejohn950330@gmail.com";
//    private static int number;
//
//    public static void createNumber(){
//        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
//    }
//
//    public int sendMail(String mail) {
//
//        createNumber();
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        try {
//            message.setFrom(senderEmail);
//            message.setRecipients(MimeMessage.RecipientType.TO, mail);
//            message.setSubject("이메일 인증");
//            String body = "";
//            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
//            body += "<h1>" + number + "</h1>";
//            body += "<h3>" + "감사합니다." + "</h3>";
//            message.setText(body,"UTF-8", "html");
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//        return message;
//
//        return 0;
//    }
//
//    public int sendMail(String mail){
//        MimeMessage message = sendMail(mail);
//        javaMailSender.send(message);
//
//        return number;
//    }
//}
