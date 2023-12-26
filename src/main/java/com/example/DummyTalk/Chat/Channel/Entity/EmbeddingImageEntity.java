package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "embedding_image")
public class EmbeddingImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "embedding_image_id")
    private Long id;        // 임베딩된 이미지 ID

    @Column(nullable = false)
    private String emText;      // 임베딩된 텍스트

    /* 임베딩 이미지와 이미지의 연관관계 (자식) */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "image_id")
    private ImageEntity imageId;
}
