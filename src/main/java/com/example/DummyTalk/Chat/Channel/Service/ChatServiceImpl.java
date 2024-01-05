package com.example.DummyTalk.Chat.Channel.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import com.example.DummyTalk.Chat.Channel.Dto.ChatDTO;
import com.example.DummyTalk.Chat.Channel.Dto.MessageRequest;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.example.DummyTalk.Chat.Channel.Controller.MessageResponse;
import com.example.DummyTalk.Chat.Channel.Dto.MessageHistoryDto;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChannelParticipantEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelParticipantRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChannelRepository;
import com.example.DummyTalk.Chat.Channel.Repository.ChatRepository;
import com.example.DummyTalk.Exception.ChatFailException;
import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import com.example.DummyTalk.User.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final ChannelRepository channelRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChannelParticipantRepository channelParticipantRepository;
    private final ModelMapper modelMapper;


    /* 채팅 데이터에 들어가는 유저 정보 Entity -> Dto 변환 */
    private ChatSenderDTO userToDto(User user) {
        return ChatSenderDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .nickname(user.getNickname())
                .userImgPath(user.getUserImgPath())
                .build();
    }

    /* 채팅 데이터 Entity -> Dto 변환 */
    private MessageHistoryDto chatToDto(ChatDataEntity chat) {
        log.info("\nsaveChatData chatToDto " + chat);
        return MessageHistoryDto.builder()
                .message(chat.getMessage())
                .chatId(chat.getChatId())
                .sender(userToDto(chat.getSender()))
                .type(chat.getType())
                .timestamp(chat.getCreatedAt())
                .build();
    }

    private ChatDataEntity convertToChannelEntity(User user, ChannelEntity channel, MessageRequest message) {
        return ChatDataEntity.builder()
                .channelId(channel)
                .message(message.getMessage())
                .sender(user)
                .language("en")
                .type("TEXT")
                .build();
    }

    /***
     *  채팅 내용 저장
     *  @param message : 채팅 내용
     *  @return : 채팅 아이디
     */
    @Transactional
    public ChatDataEntity saveChatData(MessageRequest message) {

        User user = userRepository.findByUserId((long) message.getSender());
        ChannelEntity channel = channelRepository.findByChannelId((long) message.getChannelId());

        if(user == null ) throw new ChatFailException("유저 조회에 실패하였습니다. ");
        if(channel == null ) throw new ChatFailException("채널 조회에 실패하였습니다.");
        ChatDataEntity newChat = null;
        ChatDTO chatDTO = null;
        try {
            ChatDataEntity chatEntity = convertToChannelEntity(user, channel, message);
            newChat = chatRepository.save(chatEntity);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createAt = newChat.getCreatedAt().format(formatter);

            chatDTO = ChatDTO.builder()
                                        .chatId(newChat.getChatId())
                                        .channelId(newChat.getChannelId().getChannelId())
                                        .message(newChat.getMessage())
                                        .language(newChat.getLanguage())
                                        .nickname(user.getNickname())
                                        .createdAt(createAt)
                                        .build();
        } catch (Exception e) {
            throw new ChatFailException("채팅 저장에 실패하였습니다.");
        }

        // springBoot => python
        WebClient.create()
                .post()
                .uri("http://localhost:8000/api/search/saveChatData")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(chatDTO))
                .retrieve()
                .bodyToMono(ResponseEntity.class)
                .subscribe();

        return newChat;
    }

    /***
     *  참여자 체크
     *  @param channelId : 채널 아이디
     */
    public void checkParticipant(int channelId, Long userId) {
        ChannelParticipantEntity channel =
                channelParticipantRepository.findByChannelIdAndUserId((long)channelId, userId);
//        if (channel == null) {
//            throw new ChatFailException("초대 된 채널이 아닙니다.");
//        }
    }

    /***
     *  채팅 내용 삭제
     *  @param chatId : 채팅 아이디
     *  @return 삭제된 채팅 아이디
     */
    @Override
    @Transactional
    public Long deleteChat(int channelId, int chatId) {

        ChatDataEntity chat = chatRepository.findByChannelIdAndChatId((long)channelId, (long)chatId);
        log.info("\n deleteChat chat   : {}", chat);

        if( chat == null ) throw new ChatFailException("오류가 발생하였습니다. 다시 시도해주세요.");

        return chat.delete();
    }

    /*** 채널 아이디로 조회한 채팅 데이터 리스트
     * @param channelId 채널 아이디
     * @return 채팅 데이터 리스트
     */
    public List<MessageHistoryDto> findChatData(int channelId) {

        ChannelEntity channelEntity = channelRepository.findByChannelId((long) channelId);

        if (channelEntity == null) throw new ChatFailException("채널 조회에 실패하였습니다.");

        try {
            return chatRepository.findAllByChannelId(channelEntity).stream()
                    .map(entity -> modelMapper.map(entity, MessageHistoryDto.class))
                    .collect(Collectors.toList());

        } catch (DataAccessException e) {

            log.error("Data access error: {}", e.getMessage());
            throw new ChatFailException("채널 조회에 실패하였습니다.", e);
        }
    }

    /* Chat 번역 */
    public MessageResponse translateMessage(MessageRequest chat, String nationLanguage) {

                CountDownLatch cdl = new CountDownLatch(1);
                MessageResponse response = new MessageResponse();
                WebClient.create()
                        .post()
                        .uri("http://localhost:8000/api/v1/trans/" + nationLanguage)
                        .header("Content-Type", "application/json")
                        .body(BodyInserters.fromValue(chat))
                        .retrieve()
                        .bodyToMono(MessageResponse.class)
                        .doOnTerminate(() -> cdl.countDown())
                        .subscribe((res) -> response.setMessageResponse(res.getNickname(), res.getStatus(), res.getChat()));

                log.info("{}", response);

                try {
                    cdl.await();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
        }
        return response;
    }

    @Override
    public int saveAudioChatData(MessageRequest messageRequest) {
        // 오디오 채팅 데이터를 저장하는 로직을 여기에 구현합니다.
        // 예시:
        // 1. sendChatDto에서 필요한 정보를 추출합니다.
        // 2. 채팅 데이터를 데이터베이스에 저장합니다.
        // 3. 필요한 경우 추가적인 처리를 수행합니다.
        return 0;
    }



}
