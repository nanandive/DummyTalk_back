package com.example.DummyTalk.Chat.Channel.Controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.DummyTalk.Chat.Channel.Dto.ImageDto;
import com.example.DummyTalk.Chat.Channel.Service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/img")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ImageUploadController {

    private final ChatService chatService;

    @PostMapping("/save")
    public void saveImage(@ModelAttribute ImageDto imageDto) {
//            @RequestParam(value = "fileInfo", required = false) MultipartFile[] files,
//                          @RequestParam(value = "userId", required = false) String userId,
//                          @RequestParam(value = "nickname", required = false) String nickname) {
//            @RequestParam(value = "files", required = false) MultipartFile[] fileInfo,
//            @RequestParam(value = "userId", required = false) String userId,
//            @RequestParam(value = "nickname", required = false) String nickname){
        /*
         *  @param image : 클라이언트에서 전송된 이미지 파일
         *  => ( file.name, file )
         *  @param message : 클라이언트에서 전송된 채팅 메시지 데이터
         *  => ( userId, nickname )
         *  1. 로컬에 저장
         *  2. DB에 값 저장
         *  3. 추후 성공적으로 저장되면 로컬은 삭제
         *  @param message : channelId, userId, imageUrl, Multipart */
        // 파일이 null인지는 클라이언트에서 판단
//        log.info("Received image files: {}", Arrays.toString(fileDto));
        log.info("============saveImage================================={}", imageDto);
//        chatService.saveImage( userId,nickname, image);
//        return new MessageResponse(message.getNickname(), "이미지 저장 성공", );
    }
}
