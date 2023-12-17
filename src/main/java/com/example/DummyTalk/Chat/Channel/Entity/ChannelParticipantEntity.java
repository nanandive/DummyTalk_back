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
@Builder(toBuilder = true)
public class ChannelParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 채널 아이디
    private Long lastChatId; // 참여자가 마지막으로 읽은 채팅 아이디
}
