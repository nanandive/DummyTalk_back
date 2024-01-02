package com.example.DummyTalk.Chat.Channel.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TranslatedTextDto {

    private Long id;
    private String translated_text;
    private String national_language_code;


}
