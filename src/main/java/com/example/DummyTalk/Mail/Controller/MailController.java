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

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    /* 회원가입 이메일 인증 요청 */
    @PostMapping("/userEmail")
    public ResponseEntity<ResponseDTO> authMail(@RequestBody String userEmail){

        try {
            String result = mailService.sendMail(userEmail);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK, "발급 성공", result));
        } catch (RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "발급 실패", e.getMessage()));
        }
    }

    /* 이메일 인증 확인 */
    @PostMapping("/checkNum")
    public ResponseEntity<ResponseDTO> checkNum(@RequestBody int checkNum){

        try {
            String result = mailService.checkNum(checkNum);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK, "인증 성공", result));
        } catch (RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "다시 입력해주시길 바랍니다."));
        }
    }

    /* 비밀번호 찾기 이메일 인증 */
    @PostMapping("/passwordMail")
    public ResponseEntity<ResponseDTO> passwordMail(@RequestBody Map<String,String> passwordMail){

        try {
            String result = mailService.passwordMail(passwordMail.get("userEmail"));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDTO(HttpStatus.OK, "발급 성공", result));
        } catch (RuntimeException e){

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "발급 실패", e.getMessage()));
        }
    }


}
