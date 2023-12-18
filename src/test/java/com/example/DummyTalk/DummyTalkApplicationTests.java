package com.example.DummyTalk;

import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelParticipantRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class DummyTalkApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelParticipantRepository channelParticipantRepository;

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
    @Autowired
    ChatRepository chatRepository;
    @Test
    void 채널_참여자_조회(){
        channelParticipantRepository.findAll();
    }

    @Test
    void 채팅_조회(){
        List<ChatDataEntity> chat = chatRepository.findAll();
//        chat
    }

}
