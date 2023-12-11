package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@Table(name = "image")
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;


    private String image;

    /* 이미지와 채널데이터의 연관관계(자식) */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "channel_data_id")
    private ChatDataEntity channelDataId;


    /* 이미지와 임베딩 이미지의 연관관계(부모) */
    @OneToMany( mappedBy = "imageId", fetch = FetchType.LAZY)
    private List<EmbeddingImageEntity> embeddingImageEntityList = new ArrayList<>();
}
