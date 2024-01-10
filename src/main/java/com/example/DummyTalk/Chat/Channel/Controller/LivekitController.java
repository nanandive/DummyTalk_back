package com.example.DummyTalk.Chat.Channel.Controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.livekit.server.AccessToken;
import io.livekit.server.RoomJoin;
import io.livekit.server.RoomName;

@RestController
@RequestMapping("/livekit")
public class LivekitController {
    @Value("${livekit.LK_API_KEY}")
    private String LK_API_KEY;

    @Value("${livekit.LK_API_SECRET}")
    private String LK_API_SECRET;

    @GetMapping("/getTokens")
    public ResponseEntity<?> getToken(@RequestParam String room, @RequestParam String userId, @RequestParam String nickname) {
        AccessToken token = new AccessToken(LK_API_KEY, LK_API_SECRET);
        // Fill in token information.
        token.setName(nickname);
        token.setIdentity(userId);
        token.setMetadata("metadata");
        token.addGrants(new RoomJoin(true), new RoomName(room));

        // Sign and create token string.
        System.out.println("New access token: " + token.toJwt());
        return ResponseEntity.ok(token.toJwt());
    }

}
