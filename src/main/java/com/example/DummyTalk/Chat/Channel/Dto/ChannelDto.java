package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import java.util.List;

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
