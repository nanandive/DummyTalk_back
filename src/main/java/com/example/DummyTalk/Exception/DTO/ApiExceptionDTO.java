package com.example.DummyTalk.Exception.DTO;

import lombok.*;
import org.springframework.http.HttpStatus;

/* 예외 발생 시 보낼 데이터를 담을 DTO 객체*/
@Getter
@Setter
@ToString
public class ApiExceptionDTO {

    private int status;         // 코드
    private String message;     // 에러 메세지

    public ApiExceptionDTO() {
    }

    public ApiExceptionDTO(HttpStatus status, String message) {
        this.status = status.value();
        this.message = message;
    }
}