package com.example.DummyTalk;

import java.util.Arrays;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class DummyTalkApplication {

    public static void main(String[] args) {
        SpringApplication.run(DummyTalkApplication.class, args);
    }

    @Component
    public class AppRunner implements ApplicationRunner {
        private final String URL = "http://localhost:8000/test";
        private final Environment environment;

        public AppRunner(Environment environment) {
            this.environment = environment;
        }

        @Override
        public void run(ApplicationArguments args) {
            System.out.println("===================다중 프로파일 테스트===================");
            System.out.println("Active profiles : " + Arrays.toString(environment.getActiveProfiles()));
            System.out.println("Datasource driver : " + environment.getProperty("spring.datasource.driver-class-name"));
            System.out.println("Datasource url : " + environment.getProperty("spring.datasource.url"));
            System.out.println("Datasource username : " + environment.getProperty("spring.datasource.username"));
            System.out.println("Datasource password : " + environment.getProperty("spring.datasource.password"));
            System.out.println("Server Port : " + environment.getProperty("server.port"));
            System.out.println("Default Property : " + environment.getProperty("default.string"));
            System.out.println("Common Property : " + environment.getProperty("common.string"));
            System.out.println("====================================================");

            WebClient client = WebClient.create(URL);

            client.get().retrieve().bodyToMono(String.class).onErrorReturn("Connect Fail!").subscribe(System.out::println);

            System.out.println("비동기 요청 실행");
        }


    }
}
