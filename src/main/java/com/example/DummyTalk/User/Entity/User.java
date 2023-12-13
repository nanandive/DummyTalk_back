package com.example.DummyTalk.User.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SequenceGenerator(
        name = "User_Id",
        sequenceName = "SEQ_User_Id",
        initialValue = 1,
        allocationSize = 1
)
public class User {


    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "MAIL_NO"
    )
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "user_img_path")
    private String userImgPath;

    @Column(name = "createAt")
    private String createAt;

    @Column(name = "updateAt")
    private String updateAt;


}
