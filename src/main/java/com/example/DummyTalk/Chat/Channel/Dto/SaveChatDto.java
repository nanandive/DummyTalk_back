package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
public class SaveChatDto {
    private Long chatId;
    private ChatSenderDTO sender; // User 엔터티의 ID
    private String message;
    private String language;
    private boolean isDeleted;
    private ChannelDto channelId; // ChannelEntity의 ID
    private String type;
    private String audioUrl;
    private int audioChatId;
    private LocalDateTime createdAt;
    private List<TranslatedTextDto> translatedTextList;
    private List<ImageDto> imageEntityList;
}