package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChannelDto {
    private Long channelId;
    private Long serverId;
    private String channelName;
    private int channelCount;
    private ChannelEntity.ChannelType channelType;

}
