package com.example.DummyTalk.Chat.Server.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Service.ServerService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@AllArgsConstructor
@RequestMapping("/server")
@Slf4j
public class ServerController {

    private final ServerService serverService;
    private final ChannelServiceImpl channelServiceImpl;

    /* 서버리스트 */
    @GetMapping("/list")
    public ResponseEntity<List<ServerDto>> serverList() {
        List<ServerDto> serverDtoList = serverService.findAllServer();
        System.out.println(" 서버 리스트 불러오기 : >>>>>>>>>> : " + serverDtoList);
        return ResponseEntity.ok(serverDtoList);
    }

    /* 서버생성 */
    @GetMapping("/writeModal")
    public String serverWriteModal() {
        return "/websocket/ServerWriteModal";
    }

    @PostMapping("/writePro")
    public ResponseEntity<?> serverWritePro(@ModelAttribute ServerDto serverDto,
            @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {

        ServerDto responseServerDto = null;
        if (file != null && !file.isEmpty()) {
            System.out.println("서버 생성 (컨트롤러) >>>>>>>>>> " + serverDto + file);
            responseServerDto = serverService.createServer(serverDto, file);

        } else {
            responseServerDto = serverService.createServer(serverDto);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "서버 생성", responseServerDto));
    }

    /* TODO 상세보기 */
    @GetMapping("/{id}")
    public ResponseEntity<ServerDto> getServerDetail(@PathVariable Long id) {
        ServerDto serverDto = serverService.findById(id);

        /* 채널 리스트 */

        // 실시간 서버에 접속 중인 친구 가져오기 -> @messagemapping

        // 친구 목록 가져오기

        System.out.println(" 서버에 접속 하기(컨트롤러) >>>>>>>>> : " + serverDto);
        return ResponseEntity.ok(serverDto);
    }

    /* TODO 서버 수정 */
    @PostMapping("/setting")
    public ResponseEntity<ServerSettingDto> serverSetting(@ModelAttribute ServerSettingDto serverSettingDto) {
        serverService.updateServer(serverSettingDto);
        System.out.println("서버 수정 (컨트롤러): >>>>>>>>>>>>> " + serverSettingDto);
        return ResponseEntity.ok(serverSettingDto);
    }

    /* TODO 서버 삭제 */
    @GetMapping("/delete")
    public String delete(@RequestParam Long id) {
        serverService.serverDelete(id);
        return "/wesocket/main";
    }

    /* 서버에 해당하는 채널 리스트 */
    @GetMapping("/{serverId}/channel/list")
    public ResponseEntity<List<ChannelDto>> channelList(@PathVariable Long serverId) {
        List<ChannelDto> channelDtoList = channelServiceImpl.findByChannelList(serverId);
        System.out.println("채널 리스트 (컨트롤러) >>>>>>>>> : " + channelDtoList);

        return ResponseEntity.ok(channelDtoList);

    }

    @DeleteMapping("/{serverId}/channel/{channelId}/delete")
    public ResponseEntity<List<ChannelDto>> deleteChannel(@PathVariable Long serverId, @PathVariable Long channelId) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + channelId);
        channelServiceImpl.channelDelete(channelId);
        return ResponseEntity.ok().build();
    }

}
