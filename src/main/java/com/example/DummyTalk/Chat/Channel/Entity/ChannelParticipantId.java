package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelParticipantId implements Serializable {

    private Long channelId;  // 채널 아이디
    private Long userId;     // 채널 참여자 아이디
    private Long lastChatId; // 참여자가 마지막으로 읽은 채팅 아이디

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
