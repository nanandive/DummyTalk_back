package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
public class ChatDataDto {

    public enum MessageType {
        // 메시지 타입 : 입장, 채팅
        ENTER,TALK,LEAVE
    }

//    create_at : 채팅 데이터 생성 시간
    private Long channelDataId;
    private String message;
    private String sender;
    private String language;
//    private MessageType type;
//    private LocalDateTime createAt;
//    private LocalDateTime updateAt;
//    private List<ImageDto> imageDtoList;
//    private List<EmbeddingImageDto> embeddingImageDtoList;



}
