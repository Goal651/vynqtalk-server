package com.vynqtalk.server.websocket.handler;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.vynqtalk.server.service.JwtService;

@Component
public class AuthChannelInterceptor implements HandshakeInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(AuthChannelInterceptor.class);

    private final JwtService jwtService;

    public AuthChannelInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            logger.warn("Rejected non-servlet request for WebSocket handshake");
            return false; // Reject non-servlet requests
        }

        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        String path = request.getURI().getPath();
        if (!path.contains("/ws")) {
            return true;
        }

        String token = httpServletRequest.getParameter("token");
        if (token == null || token.isEmpty()) {
            logger.warn("Token is missing or empty");
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        String userEmail = jwtService.getUsernameFromToken(token);
        logger.info("User email extracted from token: {}", userEmail);

        if (userEmail == null) {
            logger.warn("Invalid token, user email is null");
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        logger.info("User email is valid: {}", userEmail);
        attributes.put("userEmail", userEmail);
        attributes.put("stompUser", userEmail);
        return true;
    }

    @SuppressWarnings("null")
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

        if (exception == null) {
            logger.info("WebSocket connection established for client: {}", request.getRemoteAddress());
        } else {
            logger.warn("WebSocket handshake failed: {}", exception.getMessage());
        }
    }
}
