package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendChatDto {
    private int chatId;
    private String message;
    private int sender;
    private String nickname;
    private String language;
    private int channelId;
    private String audioUrl;
    private int audioChatId;

    private LocalDateTime timeStamp;
//    private List<ImageDto> imageDtoList;
//    private List<EmbeddingImageDto> embeddingImageDtoList;

}