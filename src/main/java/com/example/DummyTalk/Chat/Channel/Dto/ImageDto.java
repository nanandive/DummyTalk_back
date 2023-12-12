package com.example.DummyTalk.Chat.Channel.Dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class ImageDto {

    private Long imageId;
    private String image;



    private List<EmbeddingImageDto> embeddingImageDtoList ;

}
