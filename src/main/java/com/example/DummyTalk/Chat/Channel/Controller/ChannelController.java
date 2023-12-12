package com.example.DummyTalk.Chat.Channel.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.DummyTalk.Chat.Channel.Service.ChannelService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("/chat")
    public String main() {
        return "chat/writeForm";
        // private static Set<Long>userList = new HashSet<>();
    }

//    /* 채널 리스트 */
//    @GetMapping("/list")
//    public List<ChannelDto> channelDtoList () {
//        return channelService.findAllChannel();
//    }


}
