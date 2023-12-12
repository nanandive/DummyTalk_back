package com.example.DummyTalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class DummyTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyTalkApplication.class, args);
    }

}
