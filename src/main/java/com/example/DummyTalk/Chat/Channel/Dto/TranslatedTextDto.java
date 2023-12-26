package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
public class TranslatedTextDto {

    private Long id;
    private String translated_text;


}
