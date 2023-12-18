package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "channel_participant")
@IdClass(ChannelParticipantId.class)
@Builder(toBuilder = true)
public class ChannelParticipantEntity {
    @Id
    @Column(name = "channel_id")
    private Long channelId; // 채널 아이디
    @Id
    @Column(name = "user_id")
    private Long userId;    // 채널 참여자 아이디
    @Column(name = "last_chat_id")
    private Long lastChatId;    // 참여자가 마지막으로 읽은 채팅 아이디
}
