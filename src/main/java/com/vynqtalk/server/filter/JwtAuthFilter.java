package com.vynqtalk.server.filter;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vynqtalk.server.dto.response.JwtValidation;
import com.vynqtalk.server.model.users.User;
import com.vynqtalk.server.service.JwtService;
import com.vynqtalk.server.service.UserService;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final JwtService jwtService;
    private final UserService userService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserService userService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        String contextPath = request.getContextPath(); // "/api/v1"
        String requestURI = request.getRequestURI();   // "/api/v1/auth/login"
        String path = requestURI.substring(contextPath.length()); // "/auth/login"

        logger.debug("Path: {}", path);
        // Skip JWT processing for public and documentation routes, except /auth/check-token
        boolean isExcludedPath =
            path.startsWith("/auth/") ||
            path.startsWith("/public/") ||
            path.startsWith("/ws") ||
            path.startsWith("/actuator/") ||
            path.startsWith("/system/") ||
            path.startsWith("/swagger-ui.html") ||
            path.startsWith("/swagger-ui/") ||
            path.startsWith("/v3/api-docs/") ||
            path.startsWith("/v3/api-docs");

        if (isExcludedPath && !path.equals("/auth/check-user")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authorization header missing or invalid.\"}");
            return;
        }

        String token = authHeader.substring(7);
        JwtValidation result = jwtService.validateToken(token);

        if (!result.isValid()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String jsonError = String.format("{\"error\": \"Invalid JWT\", \"details\": \"%s\"}",
                    result.getErrorMessage());
            response.getWriter().write(jsonError);
            return;
        }
        Optional<User> userOpt = userService.getUserByEmail(result.getUsername());
        if (userOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"User not found.\"}");
            return;
        }
        User user = userOpt.get();
        logger.debug("User status: {}", user.getStatus());
        if (user.getStatus().equals("blocked")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"User is blocked.\"}");
            return;
        }

        String username = result.getUsername();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
