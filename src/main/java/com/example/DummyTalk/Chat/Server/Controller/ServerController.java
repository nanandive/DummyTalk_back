package com.example.DummyTalk.Chat.Server.Controller;

import java.security.Principal;
import java.util.List;

import com.example.DummyTalk.Chat.Server.Entity.ServerEntity;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Entity.UserChat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
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
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ServerDto>> serverList(@PathVariable Long userId) {
        List<ServerDto> serverDtoList = serverService.findServerIdByUserId(userId);
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
            @RequestParam(value = "file", required = false) MultipartFile file, Long userId
           ) throws Exception {

        log.info("file {}, serverDTO {}, userId {}", file, serverDto, userId);

        ServerDto responseServerDto = null;
        if (file != null && !file.isEmpty()) {
            System.out.println("서버 생성 (컨트롤러) >>>>>>>>>> " + serverDto + file );
            responseServerDto = serverService.createServer(serverDto, file, userId);

        } else {
            responseServerDto = serverService.createServer(serverDto);
        }

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "서버 생성", responseServerDto));
    }

    /* TODO 상세보기 */
    @GetMapping("/{id}")
    public ResponseEntity<ServerDto> getServerDetail(@PathVariable Long id) {
        ServerDto serverDto = serverService.findById(id);
        //serverService.updateMaxUser(id);

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
    /* 채널 삭제 */
    @DeleteMapping("/{serverId}/channel/{channelId}/delete")
    public ResponseEntity<List<ChannelDto>> deleteChannel(@PathVariable Long serverId, @PathVariable Long channelId) {
        System.out.println("채널 삭제 (컨트롤러) >>>>>> :" + channelId);
        channelServiceImpl.channelDelete(channelId);
        return ResponseEntity.ok().build();
    }

    /* 서버 접속 허용 */


//    /* 접속제한 */
//    @PostMapping("/joinUser/{serverId}/{userId}")
//    public ResponseEntity<?> joinServer(@PathVariable Long serverId, @PathVariable Long userId){
//        if(!serverService.checkAccess(serverId, userId)){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("유저 접속 실패");
//        }
//        return ResponseEntity.ok("유저 서버 접속 성공");
//
//    }



}
