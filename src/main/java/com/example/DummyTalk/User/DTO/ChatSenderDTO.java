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
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ChatSenderDTO {

    private Long userId;
    private String name;
    private String nickname;
    private String userImgPath;

}
