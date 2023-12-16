package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelService channelService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChannelServiceImpl channelServiceImpl;

    /* 채널 생성 */
    @PostMapping("/writePro")
    public ResponseEntity<?> serverWritePro(@ModelAttribute ChannelDto channelDto) {
        channelServiceImpl.createChannel(channelDto);
        return ResponseEntity.noContent().build();
    }

    /* 채널 삭제 */
    @DeleteMapping("/channel/{id}/delete")
    public ResponseEntity<ChannelEntity> deleteChannel(@PathVariable Long id) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + id);
        channelServiceImpl.channelDelete(id);
        return ResponseEntity.ok().build();

    }


}
