package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private MessageRequest chat;
    private String nickname;
    private String status;
    private List<MessageRequest> chatList;

    public MessageResponse(String nickname, String status, MessageRequest chat) {
        this.nickname = nickname;
        this.status = status;
        this.chat = chat;
    }

    public MessageResponse(String nickname, String status, List<MessageRequest> chatList) {
        this.nickname = nickname;
        this.status = status;
        this.chatList = chatList;
    }

    public MessageResponse(String status) {
        this.status = status;
    }

}
