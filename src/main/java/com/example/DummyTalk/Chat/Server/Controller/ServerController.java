package com.example.DummyTalk.Chat.Server.Controller;

import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Service.ServerService;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server")
public class ServerController {
    private final ServerService serverService;

    /* 서버리스트 */
    @GetMapping("/list")
    public String serverList(Model model) {
        List<ServerDto> serverDtoList = serverService.findByAll();
        model.addAttribute("list", serverDtoList);
        return "/websocket/serverList";
    }


    /* 서버생성 */
    @GetMapping("/writeModal")
    public String serverWriteModal(Model model) {
        return "/websocket/ServerWriteModal";
    }
    @PostMapping("/writePro")
    public String serverWritePro(@ModelAttribute ServerDto serverDto){
        serverService.createServer(serverDto);
        return "/websocket/main";
    }

    /* TODO 서버 삭제 */

    /* TODO 서버 수정 */





}
