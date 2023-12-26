package com.example.DummyTalk.Chat.Channel.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;

import reactor.core.publisher.Mono;

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
    public MessageResponse uploadAudio(@ModelAttribute MultipartFile file) {
        CountDownLatch cdl = new CountDownLatch(1);
        StringBuilder result = new StringBuilder();


        return new MessageResponse(null, "200", List.of());
    }

    @PostMapping("/transport/{nationalLanguage}")
    public ResponseEntity<?> translateTextToAudio(@RequestBody SendChatDto chat,
            @PathVariable String nationalLanguage) {

        byte[] result = WebClient.create()
                .post()
                .uri("http://localhost:8000/api/v1/audio/audio/" + nationalLanguage)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chat))
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


        // 리액트 클라이언트에게 바로 byte 배열을 응답
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result);
    }
    
}