package com.example.DummyTalk.Chat.Channel.Dto;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.User.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {


    private Long chatId; // 채팅 ID

    private Long channelId; // 채널 ID

    private String message;

    private String language;


}
