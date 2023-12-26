package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image")
@Builder(toBuilder = true)
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;       // 이미지 ID

    @Column(name="original_file_name")
    private String originalFileName;

    @Column(name="file_path")
    private String filePath;        // 파일 경로 ( 로컬 || S3 )

    @Column(name="saved_file_name")
    private String savedFileName;

    @Column(name="channel_id")
    private Long channelId;

    /* 이미지와 채널데이터의 연관관계(자식) */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "chat_id")
    private ChatDataEntity channelDataId;


    /* 이미지와 임베딩 이미지의 연관관계(부모) */
    @OneToMany( mappedBy = "imageId", fetch = FetchType.LAZY)
    private List<EmbeddingImageEntity> embeddingImageEntityList = new ArrayList<>();

}
