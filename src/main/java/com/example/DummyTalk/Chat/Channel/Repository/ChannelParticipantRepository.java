package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelParticipantDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelParticipantEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelParticipantRepository extends JpaRepository< ChannelParticipantEntity, ChannelParticipantId> {

    List<ChannelParticipantEntity> findAllByChannelId(Long channelId);

    ChannelParticipantEntity findByChannelIdAndUserId(Long channelId, Long userId);
}

