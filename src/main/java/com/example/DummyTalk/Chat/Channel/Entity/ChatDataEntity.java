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
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@Table(name = "chat_data")
@Builder
public class ChatDataEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;
    private String message;
    private String language;

    /* 채널데이터와 채널의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private ChannelEntity channelId;

    private String type;      // 추가: 메시지 타입 (TEXT, image, audio)

    private String audioUrl;  // 추가: 오디오 URL
    private int audioChatId;  // 추가: 오디오 채팅 ID


    @Builder.Default
    /* 채널 데이터와 번역된 텍스트의 연관관계 (부모) */
    @OneToMany( mappedBy = "channelDataId", fetch = FetchType.LAZY)
    private List<TranslatedTextEntity> translatedTextEntityList = new ArrayList<>();

    /* 채널 데이터와 이미지의 연관관계 (부모) */
    @Builder.Default
    @OneToMany( mappedBy = "channelDataId", fetch = FetchType.LAZY)
    private List<ImageEntity> imageEntityList = new ArrayList<>();




//    public static <SendChatDto> ChatDataEntity fromDto(SendChatDto sendChatDto) {
//        return ChatDataEntity.builder()
//                .sender(new User(sendChatDto.getClass()))
//                .message(sendChatDto.getMessage())
//                .language(sendChatDto.getLanguage())
//                .channelId(new ChannelEntity(sendChatDto.getChannelId()))
//                .audioUrl(sendChatDto.getAudioUrl())
//                .audioChatId(sendChatDto.getAudioChatId())
//                .build();
//    }
//
//    public void updateFromDto(SendChatDto sendChatDto) {
//        this.sender = new User(sendChatDto.getSender());
//        this.message = sendChatDto.getMessage();
//        this.language = sendChatDto.getLanguage();
//        this.channelId = new ChannelEntity(sendChatDto.getChannelId());
//        this.audioUrl = sendChatDto.getAudioUrl();
//        this.audioChatId = sendChatDto.getAudioChatId();
//    }
//

}