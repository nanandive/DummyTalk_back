package com.example.DummyTalk.Chat.Channel.Controller;

import java.util.List;

import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private SendChatDto chat;
    private String nickname;
    private String status;
    private List<SendChatDto> chatList;

    public MessageResponse(String nickname, String status, SendChatDto chat) {
        this.nickname = nickname;
        this.status = status;
        this.chat = chat;
    }

    public MessageResponse(String nickname, String status, List<SendChatDto> chatList) {
        this.nickname = nickname;
        this.status = status;
        this.chatList = chatList;
    }

    public void setMessageResponse(String nickname, String status, SendChatDto chat) {
    
        this.chat = chat;
        this.status = status;
        this.nickname = nickname;
    }

}
