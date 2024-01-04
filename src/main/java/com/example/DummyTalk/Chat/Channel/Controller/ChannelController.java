package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Common.DTO.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
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


    /* 채널 생성, 채널 타입 insert */
    @PostMapping("/writePro1")
    public ResponseEntity<?> serverWritePro2(@ModelAttribute ChannelDto channelDto) {
        ChannelDto responseChannelDTO = channelServiceImpl.createChannelType(channelDto);
        System.out.println(">>>>>>>>>" + channelDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseChannelDTO);
    }

    /* 채널 삭제 */
    @DeleteMapping("/channel/{id}/delete")
    public ResponseEntity<ChannelEntity> deleteChannel(@PathVariable Long id) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + id);
        channelServiceImpl.channelDelete(id);
        return ResponseEntity.ok().build();

    }

    /* 채널명 조회 */
    @GetMapping("/{channelId}")
    public ResponseEntity<ResponseDTO> getChannelName(@PathVariable int channelId) {
        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK,"서버 이름 조회 ",channelService.getChannelName((long)channelId)));
    }


    @PostMapping("/type")
    public ResponseEntity<?> getChannelType(@RequestParam int channelId) {
        ChannelDto channelDto = channelService.getChannelType((long)channelId);
        if (channelDto != null) {
            return ResponseEntity.ok(channelService.getChannelType((long)channelId));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
