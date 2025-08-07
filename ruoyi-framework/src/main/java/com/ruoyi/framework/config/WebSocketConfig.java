package com.ruoyi.framework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket Configuration using STOMP
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable a simple in-memory message broker to carry messages back to the client on destinations prefixed with "/topic".
        registry.enableSimpleBroker("/topic");
        // Set the application destination prefix for messages bound for @MessageMapping-annotated methods.
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register "/ws" as the STOMP endpoint. Clients will connect to this URL.
        // withSockJS() is a fallback for browsers that don't support WebSocket.
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
