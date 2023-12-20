package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.SendChatDto;
import io.netty.channel.ChannelId;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;

import com.example.DummyTalk.Chat.Channel.Service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@RequestMapping("/img")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageUploadController {

    private final ImageService imageService;

//    @MessageMapping("/image/{channelId}")
//    @SendTo("/topic/media/{channelId}")
//    public MessageResponse handleMessage(SendChatDto message
//            ,@DestinationVariable int channelId) {
//        log.info("============message================================={}", message);
//        return new MessageResponse(message.getNickname(), "이미지 전송 성공", message);
//    }

    /***
     * 이미지 저장
     * @param imageDto : channelId, userId, imageUrl, Multipart[], nickname
     * 1. 로컬에 저장
     * 2. DB에 값 저장
     * 3. 추후 성공적으로 저장되면 로컬은 삭제
     */
    @PostMapping("/save")
    public MessageResponse saveImage(@ModelAttribute ImageDto imageDto) throws IOException {
        log.info("\n saveImage \n" + imageDto);
        List<SendChatDto> saveImageToChat = imageService.saveImage(imageDto);
        return new MessageResponse(saveImageToChat.get(0).getNickname(), "이미지 전송 성공", saveImageToChat);
    }
}
