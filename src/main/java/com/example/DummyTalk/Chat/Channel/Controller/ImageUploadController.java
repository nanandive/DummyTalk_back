package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;
import com.example.DummyTalk.Chat.Channel.Service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RequestMapping("/img")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

    @PostMapping("/save")
    public void saveImage(@ModelAttribute ImageDto imageDto) {
        /*
         *  @param imageDto : 클라이언트에서 전송된 이미지 파일
         *  imageDto => fileInfo : Multipart[] , userId, channelId, nickname
         *  1. 로컬에 저장
         *  2. DB에 값 저장
         *  3. 추후 성공적으로 저장되면 로컬은 삭제
         * */
        log.info("============saveImage================================={}", imageDto);
        imageService.saveImage(imageDto);
//        return new MessageResponse(message.getNickname(), "이미지 저장 성공", );
    }
}
