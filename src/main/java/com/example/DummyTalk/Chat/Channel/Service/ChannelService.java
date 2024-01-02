package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;

public interface ChannelService {
    List<ChannelDto> findByChannelList(Long ServerId, Long userId);
    void createChannel(ChannelDto channelDto);
    ChannelDto getChannelName(Long channelId);

    ChannelDto getChannelType(Long channelId);
}