package com.example.DummyTalk.Chat.Channel.Service;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;

public interface ChatService {

    /***
     *  채팅 내용 저장
     *  @param message : 채팅 내용
     *  @return : 채팅 아이디
     */
    ChatDataEntity saveChatData(MessageRequest message);

    /*** 채널 아이디로 조회한 채팅 데이터 리스트
     * @param channelId 채널 아이디
     * @return 채팅 데이터 리스트
     */
    List<MessageHistoryDto> findChatData(int channelId);
    MessageResponse translateMessage(MessageRequest chat, String nationLanguage);

    /***
     *  채팅 내용 삭제
     *  @param chatId : 채팅 아이디
     *  @return 삭제된 채팅 아이디
     */
    Long deleteChat(int channelId, int chatId);
    int saveAudioChatData(MessageRequest message);
}