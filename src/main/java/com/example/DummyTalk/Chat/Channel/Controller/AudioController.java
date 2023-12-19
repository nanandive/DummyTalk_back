package com.example.DummyTalk.Chat.Channel.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/audio")
public class AudioController {

    private final SimpMessageSendingOperations messagingTemplate;

    public AudioController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/audioMessage")
    public void receiveAudio(@Payload byte[] audioData) {
        String directoryPath = "C:\\Users\\82106\\ee\\바탕 화면\\Audio";
        Path path = Paths.get(directoryPath, "audioFile.wav");
        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
            out.write(audioData);

            String fileUrl = "http://localhost:9999/voice/audioFile.wav";
            messagingTemplate.convertAndSend("/topic/audioPath", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend("/topic/audioError", "오류 발생: 오디오 파일 저장 실패");
        }
    }
}