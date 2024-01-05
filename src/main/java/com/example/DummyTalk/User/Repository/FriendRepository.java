package com.example.DummyTalk.User.Repository;

import com.example.DummyTalk.User.Entity.Friend;
import com.example.DummyTalk.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {


    @Query("SELECT f FROM Friend f " +
            "WHERE f.friendUserId = :userId " +
            "AND f.userId = :friendId")
    Friend findByFriend(@Param("userId")int userId, @Param("friendId")String friendId);

    @Query("SELECT f FROM Friend f " +
            "JOIN User u ON u.userId = f.userId " +
            "WHERE f.userId = :result")
    List<Friend> findByUser(@Param("result")Long result);

}
