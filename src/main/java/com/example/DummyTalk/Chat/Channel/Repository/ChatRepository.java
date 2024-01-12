package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<ChatDataEntity, Long> {


    List<ChatDataEntity> findAllByChannelId(ChannelEntity channelEntity);
    @Query("SELECT cd FROM ChatDataEntity cd JOIN FETCH cd.channelId c WHERE c.channelId = ?1 AND cd.chatId = ?2")
    ChatDataEntity findByChannelIdAndChatId(Long channelId, Long chatId);

    // 특정 시간 범위 내의 채팅 데이터를 조회하는 쿼리 메소드
}