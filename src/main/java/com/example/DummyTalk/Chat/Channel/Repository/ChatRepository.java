package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Dto.ChatDataDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatDataEntity, Long> {

}
