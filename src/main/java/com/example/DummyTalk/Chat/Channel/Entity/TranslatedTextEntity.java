package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "channelDataId")
@Table(name = "translated_text")
public class TranslatedTextEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // 번역한 텍스트ID

    private String translated_text;     // 번역된 텍스트

    /* 번역된 텍스트와 채널데이터의 연관관계(자식) */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_id")
    private ChatDataEntity channelDataId;

//text, translated_text, image, img_em_text


}
