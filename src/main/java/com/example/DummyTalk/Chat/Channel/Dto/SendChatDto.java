package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

//    private List<ImageDto> imageDtoList;
//    private List<EmbeddingImageDto> embeddingImageDtoList;

}