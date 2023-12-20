package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserEmail(String email);

    boolean existsByUserEmail(String email);

    User findByUserId(Long sender);


    User findByCredential(String credential);
}
