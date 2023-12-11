package com.example.DummyTalk.Chat.Server.Controller;

import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ServerController {
    private final ServerRepository serverRepository;

    @GetMapping("/server")
    public String ServerWriteForm(){

        return "chat/writeForm";
    }


}
