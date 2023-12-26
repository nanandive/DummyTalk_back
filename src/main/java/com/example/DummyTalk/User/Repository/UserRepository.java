package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.DTO.ChatSenderDTO;
import com.example.DummyTalk.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserEmail(String email);

    boolean existsByUserEmail(String email);

    User findByUserId(Long sender);

    User findByCredential(String credential);

    @Query("SELECT u FROM User u " +
            "JOIN Friend f ON f.userId = u.userId " +
            "WHERE u.userId = :id")
    List<User> findByFriend(int id);
}
