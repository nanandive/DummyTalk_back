package com.example.DummyTalk.Chat.Channel.Dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ChannelDto {

    private String channelName;
    private String userCount;

    private List<ChatDataDto> chatDataDtoList;


    @Builder
    public ChannelDto(String channelName, String userCount, List<ChatDataDto> chatDataDtoList) {
        this.channelName = channelName;
        this.userCount = userCount;
        this.chatDataDtoList = chatDataDtoList;
    }

}
