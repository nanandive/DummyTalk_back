package com.example.DummyTalk.Chat.Channel.Dto.WebRTC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayloadRequestDTO {
    String type;
    String dest;
    String source;
    long channelId;
    AudioData data;
}
