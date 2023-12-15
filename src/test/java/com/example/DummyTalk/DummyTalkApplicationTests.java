package com.example.DummyTalk;

import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class DummyTalkApplicationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void createUserTest() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setNickname("user" + i);
            user.setPassword("password" + i);
            user.setUserEmail("user" + i + "@example.com");
            user.setUserImgPath("/img/user" + i + ".jpg");
            // user.setUserName("User " + i);
            user.setUserPhone("010-000" + i + "-000" + i);
            user.setCreateAt(LocalDateTime.now());
            user.setUpdateAt(LocalDateTime.now());

            userRepository.save(user);
        }
    }

}
