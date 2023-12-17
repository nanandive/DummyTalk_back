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
    private Long imageId;       // 이미지 ID

    @Column(name="original_file_name")
    private String originalFileName;

    @Column(name="file_path")
    private String filePath;

    @Column(name="saved_file_name")
    private String savedFileName;

    /* 이미지와 채널데이터의 연관관계(자식) */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_id")
    private ChatDataEntity channelDataId;


    /* 이미지와 임베딩 이미지의 연관관계(부모) */
    @OneToMany( mappedBy = "imageId", fetch = FetchType.LAZY)
    private List<EmbeddingImageEntity> embeddingImageEntityList = new ArrayList<>();
}
