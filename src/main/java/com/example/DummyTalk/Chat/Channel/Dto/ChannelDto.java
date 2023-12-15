package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChannelDto {

    private Long ServerId;
    private String channelName;
    private int channelCount;



}
