package com.example.DummyTalk.Chat.Channel.Service;

import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;  // 이 부분 수정
import java.util.UUID;

@Service
public class ChatVoiceService {

    @Value("${audio.upload.directory}")
    private String audioUploadDirectory;

    public int saveAudioChatData(MessageHistoryDto message) {
        // Save audio chat data to the database and return the chat ID
        // Your implementation here
        return 0; // Placeholder value
    }

    public String saveAudioFile(MultipartFile audioFile) throws IOException {
        // Generate a unique filename
        String fileName = UUID.randomUUID().toString() + ".wav";

        // Save the audio file to the specified directory
        File file = new File(audioUploadDirectory + File.separator + fileName);
        audioFile.transferTo(file);

        // Return the URL of the saved audio file
        return "/audio/" + fileName;
    }
}