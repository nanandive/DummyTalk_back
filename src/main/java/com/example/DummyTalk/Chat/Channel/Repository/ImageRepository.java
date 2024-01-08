package com.example.DummyTalk.Chat.Channel.Repository;

import com.example.DummyTalk.Chat.Channel.Entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {

//    @Query("SELECT img FROM ImageEntity img JOIN FETCH ChannelEntity c ON img.channelId WHERE c.channelId = ?1")
    List<ImageEntity> findByChannelId(Long channelId);

}
