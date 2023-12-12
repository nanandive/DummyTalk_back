package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@Builder
public class ImageDto {

    private Long imageId;
    private String image;



    private List<EmbeddingImageDto> embeddingImageDtoList ;

}
