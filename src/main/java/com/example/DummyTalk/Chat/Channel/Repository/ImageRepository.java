package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

    List<ImageEntity> findAllByChannelId(Long channelId);
}
