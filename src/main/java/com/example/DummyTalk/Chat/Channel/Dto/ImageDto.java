package com.example.DummyTalk.Chat.Channel.Dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder(toBuilder = true)
public class ImageDto {
    private Long imageId;
    private String originalFileName;
    private String filePath;
    private String savedFileName;
    private List<EmbeddingImageDto> embeddingImageDtoList ;
}
