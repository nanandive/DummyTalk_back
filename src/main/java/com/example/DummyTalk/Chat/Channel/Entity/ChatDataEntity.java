package com.example.DummyTalk.Chat.Channel.Entity;

import com.example.DummyTalk.Common.Entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "chat_data")
public class ChatDataEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long channelDataId;

    private String sender;
    private String contents;
    private String file;
    private String language;


    /* 채널데이터와 채널의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelEntity channelId;
}
