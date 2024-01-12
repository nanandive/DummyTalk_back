package com.example.DummyTalk.Chat.Channel.Controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    private MessageRequest chat;
    private String nickname;
    private String status;
    @JsonFormat(pattern = "YYYY-MM-DD HH:mm:ss")
    private LocalDateTime createdAt;
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

    public void setMessageResponse(String nickname, String status, MessageRequest chat) {

        this.chat = chat;
        this.status = status;
        this.nickname = nickname;
    }

}
