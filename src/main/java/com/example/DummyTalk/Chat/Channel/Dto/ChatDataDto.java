package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder(toBuilder = true)
public class ChatDataDto {

    public enum MessageType {
        // 메시지 타입 : 입장, 채팅
        ENTER,TALK,LEAVE
    }

    private Long chatId;
    private String message;
    private String sender;
    private String language;
    private MessageType type;
    private Long channelId; // 채팅 데이터 입력을 위한 채널 아이디
    private List<ImageDto> imageDtoList;
    private List<EmbeddingImageDto> embeddingImageDtoList;




}
