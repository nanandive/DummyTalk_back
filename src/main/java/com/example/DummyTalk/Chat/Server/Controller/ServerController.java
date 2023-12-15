package com.example.DummyTalk.Chat.Server.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelService;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Service.ServerService;
import com.example.DummyTalk.Chat.Server.repository.ServerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/server")
@Slf4j
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
    public String serverWriteModal() {
        return "/websocket/ServerWriteModal";
    }
    @PostMapping("/writePro")
    public String serverWritePro(@ModelAttribute ServerDto serverDto,
                                 @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        if (file !=null  && !file.isEmpty()  ) {
            System.out.println("서버 생성 (컨트롤러) >>>>>>>>>> " + serverDto + file);
            serverService.createServer(serverDto, file);

        }else {
            serverService.createServer(serverDto);
        }
        return "/wesocket/server";
    }

    /* TODO 상세보기 */
    @GetMapping("/{id}")
    public ResponseEntity<ServerDto> getServerDetail(@PathVariable Long id){

        ServerDto serverDto = serverService.findbyId(id);

        // 채널 목록 가져오기
        List<ChannelDto> channelDtoList = channelService.findAllChannel(id);

        // 실시간 서버에 접속 중인 친구 가져오기 -> @messagemapping

        // 친구 목록 가져오기

        System.out.println(" 서버에 접속 하기(컨트롤러) >>>>>>>>> : " + serverDto);
        return ResponseEntity.ok(serverDto);
    }

    /* TODO 서버 수정 */
    @PostMapping("/setting")
    public ResponseEntity<ServerSettingDto> serverSetting (@ModelAttribute ServerSettingDto serverSettingDto,
                                                           @RequestParam(value = "file", required = false) MultipartFile file) {
            serverService.updateServer(serverSettingDto, file);
            System.out.println("서버 수정 (컨트롤러): >>>>>>>>>>>>> " + serverSettingDto + file);
            return ResponseEntity.ok(serverSettingDto);
    }

    /* TODO 서버 삭제 */
    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        serverService.serverDelete(id);
        return "/wesocket/main";
    }


}
