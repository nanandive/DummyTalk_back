package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelParticipantDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

public interface ChatService {
    int saveChatData(SendChatDto message);
    List<MessageHistoryDto> findChatData(int channelId, String userId);
    MessageResponse translateMessage(SendChatDto chat, String nationLanguage);
    void checkParticipant(int channelId, Long userId);
    Object deleteChat(int chatId);
    int saveAudioChatData(SendChatDto message);
}