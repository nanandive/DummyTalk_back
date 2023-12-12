package com.example.DummyTalk.Chat.Server.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "server_id")
    private Long id;

    @Column(nullable = false)
    private String ServerName;

    @Column(nullable = false)
    private String user_id;

    @Column(nullable = false)
    private String invitedUser;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
