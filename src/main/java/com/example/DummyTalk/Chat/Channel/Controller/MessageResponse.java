package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;

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

}
