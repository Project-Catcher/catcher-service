package com.catcher.infrastructure.jwt;

import com.catcher.common.exception.BaseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import static com.catcher.common.exception.BaseResponseStatus.INVALID_JWT;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;
    private final UserDetailsService userDetailsService;

    public Authentication getAuthentication(String token) {
        String userPrincipal = Jwts.parser().
                setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            checkToken(token);
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new BaseException(INVALID_JWT);
        }
    }

    private void checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            throw new BaseException(INVALID_JWT);
        }
    }
}
