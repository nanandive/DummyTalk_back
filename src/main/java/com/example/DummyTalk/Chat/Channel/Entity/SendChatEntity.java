package com.example.DummyTalk.Chat.Channel.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_data")
public class SendChatEntity {
    public enum MessageType {
        // 메시지 타입 : 입장, 채팅
        ENTER, TALK, LEAVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //    create_at : 채팅 데이터 생성 시간
    private Long channelDataId;
    private Long sender;      // 보낸 사람
    //        private MessageType type;
//        private String language;   // 번역 언어
//        private LocalDateTime createAt;
//        private LocalDateTime updateAt;
    private String message;
//    private String content;     // 음성 번역 여부
//        private List<ImageDto> imageDtoList;
//        private List<EmbeddingImageDto> embeddingImageDtoList;


}
