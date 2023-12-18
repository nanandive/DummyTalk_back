package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChannelParticipantDto {
    private Long userId;     // 채널 참여자 아이디
    private Long channelId;  // 채널 아이디
    private Long lastChatId; // 참여자가 마지막으로 읽은 채팅 아이디
}
