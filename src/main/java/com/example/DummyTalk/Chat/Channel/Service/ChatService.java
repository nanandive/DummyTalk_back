package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

import java.util.List;

public interface ChatService {
    int saveChatData(SendChatDto message);
    List<MessageHistoryDto> findChatData(int channelId);
}