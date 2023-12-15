package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;

import java.util.List;

public interface ChannelService {
    List<ChannelDto> findByChannelList(Long ServerId);
}