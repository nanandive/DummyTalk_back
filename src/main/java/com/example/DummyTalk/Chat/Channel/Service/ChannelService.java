package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

public interface ChannelService {
    List<ChannelDto> findByChannelList(Long ServerId);

    int saveChatData(SendChatDto message);

    List<ChatListDto> findChatData(int channelId);

    void createChannel(ChannelDto channelDto);
}