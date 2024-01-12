package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Service.ChatVoiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/chat/voice")
public class ChatVoiceController {

    private final ChatVoiceService chatVoiceService;

    public ChatVoiceController(ChatVoiceService chatVoiceService) {
        this.chatVoiceService = chatVoiceService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVoiceFile(@RequestBody MultipartFile voiceFile) {
        try {
            String voiceUrl = chatVoiceService.saveAudioFile(voiceFile);
            return ResponseEntity.ok(voiceUrl);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Voice file upload failed");
        }
    }
}

//    @PostMapping("/send")
//    public ResponseEntity<MessageResponse> sendVoiceMessage(@RequestBody MessageHistoryDto audioMessage) {
//        int audioChatId = chatVoiceService.saveAudioChatData(audioMessage);
//        audioMessage.setAudioChatId(audioChatId);
//
//        // Your additional logic here, if needed
//
//        return ResponseEntity.ok(new MessageResponse(audioMessage.getSender().getNickname(), "Voice message sent successfully", audioMessage));
//    }

//db저장용