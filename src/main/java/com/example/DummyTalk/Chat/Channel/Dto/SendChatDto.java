package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendChatDto {

    private String message;
    private int sender;
    private String language;
    private int channelId;
//    private List<ImageDto> imageDtoList;
//    private List<EmbeddingImageDto> embeddingImageDtoList;



}