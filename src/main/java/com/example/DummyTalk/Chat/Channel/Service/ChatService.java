package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

public interface ChatService {
    int saveChatData(SendChatDto message);
    List<MessageHistoryDto> findChatData(int channelId);
    MessageResponse translateMessage(SendChatDto chat, String nationLanguage);
}