package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@ToString
@Builder(toBuilder = true)
public class ChannelDto {

    private String channelName;
    private int channelCount;

    private List<ChatDataDto> chatDataDtoList;


    @Builder
    public ChannelDto(String channelName, int channelCount, List<ChatDataDto> chatDataDtoList) {
        this.channelName = channelName;
        this.channelCount = channelCount;
        this.chatDataDtoList = chatDataDtoList;
    }

}
