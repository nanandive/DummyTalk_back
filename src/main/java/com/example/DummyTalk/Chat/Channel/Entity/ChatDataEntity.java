package com.example.DummyTalk.Chat.Channel.Entity;

import com.example.DummyTalk.Common.Entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "chat_data")
public class ChatDataEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelDataId;     // chatData ID

    private String sender;          // 보낸사람
    private String message;         // 메시지
    private String language;        // 언어


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
}
