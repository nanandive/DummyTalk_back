package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

public interface ChannelService {
    List<ChannelDto> findByChannelList(Long ServerId);
    void createChannel(ChannelDto channelDto);

}