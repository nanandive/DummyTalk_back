package com.example.DummyTalk.Chat.Channel.Entity;

import com.example.DummyTalk.Common.Entity.BaseTimeEntity;
import com.example.DummyTalk.User.Entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_data")
@Builder(toBuilder = true)
public class ChatDataEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;
//    private String sender;
    private String message;
    private String language;




    /* 채널데이터와 채널의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelEntity channelId;

    /* 채널 데이터와 번역된 텍스트의 연관관계 (부모) */
    @OneToMany( mappedBy = "channelDataId", fetch = FetchType.LAZY)
    private List<TranslatedTextEntity> translatedTextEntityList = new ArrayList<>();

    /* 채널 데이터와 이미지의 연관관계 (부모) */

    @OneToMany( mappedBy = "channelDataId", fetch = FetchType.LAZY)
    private List<ImageEntity> imageEntityList = new ArrayList<>();

    public ChatDataEntity build() {
        ChatDataEntity entity = new ChatDataEntity();
        entity.channelId = this.channelId;
        entity.message = this.message;
        entity.sender = this.sender;
        entity.language = this.language;
        return entity;
    }


}