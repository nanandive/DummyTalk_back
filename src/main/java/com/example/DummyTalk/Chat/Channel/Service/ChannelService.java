package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Dto.ChatListDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

import java.util.List;

public interface ChannelService {
    List<ChannelDto> findByChannelList(Long ServerId);

    void saveChatData(SendChatDto message);

    List<ChatListDto> findChatData(int channelId);
}