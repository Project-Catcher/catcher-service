package com.catcher.infrastructure.jwt.filter;

import com.catcher.core.db.DBManager;
import com.catcher.infrastructure.jwt.JwtTokenProvider;
import com.catcher.utils.KeyGenerator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.catcher.utils.KeyGenerator.*;
import static com.catcher.utils.KeyGenerator.AuthType.*;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final DBManager dbManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        if (accessToken != null && jwtTokenProvider.validateToken(accessToken) && !isBlackList(accessToken)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isBlackList(String accessToken) {
        return dbManager.getValue(generateKey(accessToken, BLACK_LIST_ACCESS_TOKEN)).isPresent();
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader(AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            header = header.substring(7);
        }
        return header;
    }
}
