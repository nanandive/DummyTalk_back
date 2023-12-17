package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelParticipantDto {
    private int userId;     // 채널 참여자 아이디
    private int channelId;  // 채널 아이디
    private int lastChatId; // 참여자가 마지막으로 읽은 채팅 아이디
}
