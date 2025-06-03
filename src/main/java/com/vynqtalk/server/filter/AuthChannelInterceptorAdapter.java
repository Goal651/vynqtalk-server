package com.vynqtalk.server.filter;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.vynqtalk.server.service.JwtService;

@Component
public class AuthChannelInterceptorAdapter implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService;


    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,@NonNull ServerHttpResponse response,
           @NonNull WebSocketHandler wsHandler,@NonNull Map<String, Object> attributes) throws Exception {
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false; // Reject non-servlet requests
        }

        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
        if (!request.getURI().getPath().startsWith("/ws")) {
            return true; // Allow non-websocket requests
        }

        String token = httpServletRequest.getParameter("token");

        if (token == null || token.isEmpty()) {
            System.out.println("Token is missing or empty");
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        String userEmail = jwtService.getUsernameFromToken(token);
        System.out.println("User email extracted from token: " + userEmail);

        if (userEmail == null) {
            System.out.println("Invalid token, user ID is null");
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        System.out.println("User ID is valid: " + userEmail);
        attributes.put("userEmail", userEmail);
           attributes.put("stompUser", userEmail);
        return true;
    }

    @SuppressWarnings("null")
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {

        if (exception == null) {
            System.out.println("WebSocket connection established for client: {}" + request.getRemoteAddress());
        } else {
            System.out.println("WebSocket handshake failed: {}" + exception.getMessage());
        }
    }
}
