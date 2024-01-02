package com.example.DummyTalk.Chat.Channel.Dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageHistoryDto {

    private Long chatId;
    private String message;
    private LocalDateTime timestamp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ChatSenderDTO sender;
    private String type;

    private List<TranslatedTextDto> translatedTextList;
    // Fields related to audio messages
    private String audioUrl; // URL of the audio file
    private int audioChatId; // Unique ID for the audio chat


}
