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
import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelParticipantId implements Serializable {

    private Long channelId;  // 채널 아이디
    private Long userId;     // 채널 참여자 아이디
    private Long lastChatId; // 참여자가 마지막으로 읽은 채팅 아이디

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelParticipantId that = (ChannelParticipantId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, channelId);
    }
}
