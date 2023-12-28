package com.example.DummyTalk.Chat.Server.Controller;

import com.example.DummyTalk.Chat.Channel.Dto.ChannelDto;
import com.example.DummyTalk.Chat.Channel.Service.ChannelServiceImpl;
import com.example.DummyTalk.Chat.Server.Dto.ServerDto;
import com.example.DummyTalk.Chat.Server.Dto.ServerSettingDto;
import com.example.DummyTalk.Chat.Server.Service.ServerService;
import com.example.DummyTalk.Common.DTO.ResponseDTO;
import com.example.DummyTalk.User.DTO.UserDTO;
import com.example.DummyTalk.User.DTO.UserServerCodeDto;
import com.example.DummyTalk.User.Entity.UserChat;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam Long id, @RequestParam Long userId) {
        try {
            serverService.serverDelete(id, userId);
            return ResponseEntity.ok().body("서버가 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("서버 삭제 권한이 없습니다.");
        }
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

    /* 서버 초대 */
    @PostMapping("/invitedUser")
    public ResponseEntity<?> invitedUser(@RequestBody UserServerCodeDto userServerCodeDto) {

            try {
                serverService.addUserInvitedCode(userServerCodeDto);
                return ResponseEntity.ok().body("(성공) 초대코드 : " + userServerCodeDto);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("초대 실패");
            }
        }

    /* 서버 초대된 유저 리스트 */
    @GetMapping("/access/{serverId}")
    public ResponseEntity<List<UserDTO>> getAccessUser(@PathVariable Long serverId) {
        List<UserDTO> invitedUserDto = serverService.getInvitedUser(serverId);
        System.out.println("서버에 초대된 유저 리스트 (컨트롤러) >>>>> : " + invitedUserDto);
        return ResponseEntity.ok(invitedUserDto);
    }

    /* 서버 유저 강퇴(방장만 강퇴가능) */
    @PostMapping("/resignUser/{serverId}")
    public ResponseEntity<?> resignUser(@PathVariable Long serverId, @RequestBody UserDTO userDto){
        String userEmail = userDto.getUserEmail();
        Long userId = userDto.getUserId();      // 강퇴하는 사람의 Id
        Long serverUserId = serverService.findById(serverId).getUserId();   // 서버를 생성한 사람의 Id
        System.out.println("(컨트롤러) 강퇴 resignUser : " + userId);
        System.out.println("(컨트롤러) 강퇴 MainUser : " + serverUserId);
        // 권한 체크 (서버를 생성한 사람인지 확인)
        if(userId.equals(serverUserId)) {
            try {
//                serverService.deleteUser(userDto);
                serverService.deleteUser(serverId, userEmail);
                return ResponseEntity.ok().body("사용자 강퇴 성공");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 강퇴 실패.");
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("권한이 없습니다.");
    }



}
