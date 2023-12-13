package com.example.DummyTalk.Chat.Channel.Entity;

import java.util.ArrayList;
import java.util.List;

import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.Common.Entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "channel")
public class ChannelEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long channelId;     //채널 ID

    @Column(nullable = false)
    private String ChannelName;     // 채널이름

    @Column(nullable = false)
    private int channelCount;       // 채널에 접속한 유저 수


    /* 서버와의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private ServerEntity serverEntity;

    /* 채널과 채널 데이터와의 연관관계 (부모) */
    @OneToMany( mappedBy = "channelId", fetch = FetchType.LAZY)
    private List<ChatDataEntity> chatDataEntityList = new ArrayList<>();



}
