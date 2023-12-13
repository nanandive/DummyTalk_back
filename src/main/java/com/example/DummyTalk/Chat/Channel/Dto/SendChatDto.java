package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class SendChatDto {

    private Long channelDataId;
    private Long sender;
    private String message;
}
