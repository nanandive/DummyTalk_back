package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatDataEntity, Long> {
        List<ChatDataEntity> findByChannelId(ChannelEntity channelEntity);

//    ChatSenderDTO findBySender(ChatSenderDTO sender);
        }