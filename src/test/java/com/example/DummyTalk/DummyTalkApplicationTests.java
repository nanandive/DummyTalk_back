package com.example.DummyTalk;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelParticipantRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class DummyTalkApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelParticipantRepository channelParticipantRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createUserTest() {
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setNickname("유저닉네임" + i);
            user.setPassword(passwordEncoder.encode("1234"));
            user.setUserEmail("user" + i + "@test.com");
            user.setName("User " + i);
            user.setUserPhone("111"+i);
            user.setCreateAt(LocalDateTime.now());

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
    }

    @Test
    void 채팅_삭제(){
        ChatDataEntity chat = chatRepository.findByChannelIdAndChatId((long)14, (long)41);
        System.out.println(chat);
        chat.delete();
    }

}
