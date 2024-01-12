package com.example.DummyTalk.Chat.Channel.Dto.WebRTC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IceCandidate {
    private String candidate;
    private String sdpMid;
    private int sdpMLineIndex;
}