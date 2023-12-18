package com.example.DummyTalk.Chat.Channel.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AudioController {

    private final SimpMessageSendingOperations messagingTemplate;

    public AudioController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/audioMessage")
    public void receiveAudio(@Payload byte[] audioData) {
        String directoryPath = "YOUR_SERVER_AUDIO_DIRECTORY_PATH";
        Path path = Paths.get(directoryPath, "audioFile.wav");
        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
            out.write(audioData);

            String fileUrl = "YOUR_SERVER_AUDIO_URL";
            messagingTemplate.convertAndSend("/topic/audioPath", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            messagingTemplate.convertAndSend("/topic/audioError", "오류 발생: 오디오 파일 저장 실패");
        }
    }
}