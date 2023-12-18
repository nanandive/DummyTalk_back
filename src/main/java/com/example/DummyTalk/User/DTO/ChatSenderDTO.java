package com.example.DummyTalk.User.DTO;


import com.example.DummyTalk.User.Entity.UserChat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatSenderDTO {

    private Long sender;
    private String name;
    private String nickname;
    private String userImgPath;

    public ChatSenderDTO build() {
        ChatSenderDTO dto = new ChatSenderDTO();
        dto.sender = this.sender;
        dto.name = this.name;
        dto.nickname = this.nickname;
        dto.userImgPath = this.userImgPath;
        return dto;
    }

}
