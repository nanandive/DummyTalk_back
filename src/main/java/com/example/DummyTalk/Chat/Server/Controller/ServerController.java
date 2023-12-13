package com.example.DummyTalk.Chat.Server.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Service.ServerService;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server")
public class ServerController {

    private final ServerService serverService;
    private final ChannelService channelService;

    /* 서버리스트 */
    @GetMapping("/list")
    public ResponseEntity<List<ServerDto>> serverList() {
        List<ServerDto> serverDtoList = serverService.findAllServer();
        System.out.println(" 리스트 불러오기 : >>>>>>>>>> : " + serverDtoList);
        return ResponseEntity.ok(serverDtoList);
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

    /* TODO 상세보기 */
    @GetMapping("/{id}")
    public ResponseEntity<ServerDto> getServerDetail(@PathVariable Long id){

        ServerDto serverDto = serverService.findbyId(id);

        // 채널 목록 가져오기
        List<ChannelDto> channelDtoList = channelService.findAllChannel(id);
        // 실시간 서버에 접속 중인 친구 가져오기 -> @messagemapping

        // 친구 목록 가져오기

        System.out.println(" 서버에 접속 하기 >>>>>>>>> : " + serverDto);
        return ResponseEntity.ok(serverDto);
    }

    /* TODO 서버 수정 */
    @GetMapping("/modify")
    public String serverModify (Model model) {

        return "/websocket/main";
    }

    /* TODO 서버 삭제 */



}
