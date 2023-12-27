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
            "WHERE f.friendUserId = :id " +
            "AND f.accept = 'Y'")
    List<User> findByFriends(@Param("id") int id);

    @Query("SELECT u FROM User u " +
            "JOIN Friend f ON f.userId = u.userId " +
            "WHERE f.friendUserId = :userId " +
            "AND f.accept = 'N'")
    List<User> findByFriendRequest(@Param("userId") int userId);

}
