package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelServiceImpl channelServiceImpl;


    /* 채널 생성 */
    @PostMapping("/writePro")
    public String serverWritePro(@ModelAttribute ChannelDto channelDto){
        channelServiceImpl.createChannel(channelDto);
        return "null";

    }

    /* 채널 삭제 */
    @DeleteMapping("/channel/{id}/delete")
    public ResponseEntity<ChannelEntity> deleteChannel(@PathVariable Long id) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + id);
        channelServiceImpl.channelDelete(id);
        return ResponseEntity.ok().build();

    }
}
