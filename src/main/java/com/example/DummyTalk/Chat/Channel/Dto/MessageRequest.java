package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    private Long chatId;
    private String message;
    private int sender;
    private String nickname;
    private String profileImage;
    private int channelId;
    private LocalDateTime timeStamp;
    private String type;
    private String filePath;
    private int imageId;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private LocalDateTime createdAt;
    private List<MessageRequest> chatList;
    private MultipartFile[] fileInfo;
    private MultipartFile file;

    /* 오디오 저장 */
    private String language;
    private String audioUrl;
    private int audioChatId;

}