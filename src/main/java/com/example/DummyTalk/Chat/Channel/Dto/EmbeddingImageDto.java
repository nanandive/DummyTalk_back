package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
public class EmbeddingImageDto {
    private Long id;
    private String emText;

}
