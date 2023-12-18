package com.example.DummyTalk.Chat.Channel.Entity;

import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Common.Entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"serverEntity", "chatDataEntityList"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "channel")
public class ChannelEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long channelId; // 채널 ID

    @Column(nullable = false)
    private String channelName; // 채널 이름

    @Column(nullable = false)
    private int channelCount; // 채널에 접속한 유저 수

    @Column(name = "server_id", nullable = false)
    private long serverId;

    /* 서버와의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id", updatable = false, insertable = false)
    private ServerEntity serverEntity;

    /* 채널과 채널 데이터와의 연관관계 (부모) */
    @Builder.Default
    @OneToMany(mappedBy = "channelId", fetch = FetchType.LAZY)
    private List<ChatDataEntity> chatDataEntityList = new ArrayList<>();
}