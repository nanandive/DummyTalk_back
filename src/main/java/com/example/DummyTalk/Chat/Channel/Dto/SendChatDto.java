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
    private int channelId;
    private LocalDateTime timeStamp;

    /* 오디오 저장 */
    private String language;
    private String audioUrl;
    private int audioChatId;

    /* 이미지 저장 */
    private Long imageId;
    private String originalFileName;
    private String filePath;
    private String savedFileName;
}