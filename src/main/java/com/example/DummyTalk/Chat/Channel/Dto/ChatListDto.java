package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
@Data
@ToString
@Builder(toBuilder = true)
public class ChatListDto {

    private Long chatId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ChatSenderDTO sender;

}
