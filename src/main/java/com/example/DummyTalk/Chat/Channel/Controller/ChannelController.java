package com.example.DummyTalk.Chat.Channel.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/channel")
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("/")
    public String main() {
        return "/chat/writeForm";
    }

        /* 진행중 */
//    @GetMapping("/list")
//    public String getList(Model model){
//        log.info("getList()");
//
//        List<ChannelDto> list ;
//        model.addAllAttributes("list", list);
//        return "channel/list";
//    }

}
