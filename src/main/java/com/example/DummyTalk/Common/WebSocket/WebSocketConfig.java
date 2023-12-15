package com.example.DummyTalk.Common.WebSocket;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;



@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("http://localhost:3000/websocket/**")         // CORS 허용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {    // 메시지 브로커 설정

        registry.enableSimpleBroker("/topic", "/queue");  // 구독 요청
        registry.setApplicationDestinationPrefixes("/app");                 // 클라이언트 -> 서버로 메시지를 보낼 때 붙여줄 prefix
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new WebSocketAuthInterceptor());
//    }

}
