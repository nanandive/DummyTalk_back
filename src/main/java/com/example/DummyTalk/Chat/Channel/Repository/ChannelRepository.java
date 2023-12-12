package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import jakarta.annotation.PostConstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public interface ChannelRepository extends JpaRepository<ChannelEntity, Long> {




}
