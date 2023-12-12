package com.example.DummyTalk.Chat.Server.Entity;

import com.example.DummyTalk.Chat.Channel.Entity.ChannelEntity;
import com.example.DummyTalk.Chat.Channel.Entity.ChatDataEntity;
import com.example.DummyTalk.Common.Entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "server")
public class ServerEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serverId;

    private String serverName;
    private String userName;
    private String invitedCode;
    private long userCount;


    /* 유저와 서버의 관계 */

    /* 채널과의 연관관계 (부모) */
    @OneToMany(mappedBy = "serverEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ChannelEntity> channelEntityList = new ArrayList<>();


}
