package com.example.DummyTalk.Chat.Channel.Dto.WebRTC;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AudioData {
    private String message;
    private SessionDescription sdp;
    private IceCandidate candidate;
}
