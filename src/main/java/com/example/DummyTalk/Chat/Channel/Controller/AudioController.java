package com.example.DummyTalk.Chat.Channel.Controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;


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

        String directoryPath = "C:\\Audio";
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
    public void uploadAudio(@ModelAttribute MultipartFile file) {
        String uploadPath = "C:\\Audio\\";
        String fileName = UUID.randomUUID().toString() + ".wav";

        try {
            File temp = File.createTempFile(file.getOriginalFilename(), ".wav");
            // 파일 저장
            file.transferTo(new File(temp.getPath()));
            WebClient.create()
                    .post()
                    .uri("http://localhost:8000/api/v1/audio/kor_Hand")  // FastAPI 엔드포인트 경로
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", new UrlResource(temp.toURI())))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/transport/{nationalLanguage}")
    public ResponseEntity<?> translateTextToAudio(String text, @PathVariable String nationalLanguage) {
        DataBuffer audioData = WebClient.create()
                .post()
                .uri("/receive-file")
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(DataBuffer.class)
                .block();
                // .asByteBuffer()
                // .array();

        audioData.readableByteBuffers();
        // 리액트 클라이언트에게 바로 byte 배열을 응답
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(audioData);
    }
    
}