package com.example.DummyTalk.Common.WebSocket;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")          // 앤드포인트
                .setAllowedOrigins("http://localhost:3000") // CORS 허용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 -> 메시지 받을 때
        registry.enableSimpleBroker("/topic");  // 구독 요청 prefix
        registry.setApplicationDestinationPrefixes("/app");     // 클라이언트 -> 서버로 메시지를 보낼 때 붙여줄 prefix
    }
}
