package com.example.DummyTalk.Chat.Channel.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/audio")
public class AudioController {

    private final SimpMessageSendingOperations messagingTemplate;

    public AudioController(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/audioMessage")
    @SendTo("/topic/audio/{channelId}")
    public void receiveAudio(@Payload byte[] data) {
        
        String directoryPath = "C:\\Users\\82106\\ee\\바탕 화면\\Audio";
        Path path = Paths.get(directoryPath, "audioFile.wav");
        try (FileOutputStream out = new FileOutputStream(path.toFile())) {
            out.write(data);
            // String fileUrl = "http://localhost:9999/voice/audioFile.wav";
            // messagingTemplate.convertAndSend("/topic/audioPath", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            // messagingTemplate.convertAndSend("/topic/audioError", "오류 발생: 오디오 파일 저장 실패");
        }
    }

    @PostMapping("/upload")
    public void postMethodName(@ModelAttribute MultipartFile file) {
        String uploadPath = "C:\\Users\\82106\\ee\\바탕 화면\\Audio\\";
        String fileName = file.getOriginalFilename();

        try {
            // 파일 저장
            file.transferTo(new File(uploadPath + fileName + ".webm"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

