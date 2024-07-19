package com.example.flab.soft.shoppingmallfashion.auth.jwt;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokenBuildDto;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TokenProvider {
    private final String secret;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validation-time}")Long accessExpirationTime,
            @Value("${jwt.refresh-token-validation-time}") Long refreshExpirationTime) {
        this.secret = secret;
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public String createAccessToken(TokenBuildDto tokenBuildDto) {
        return createToken(tokenBuildDto, accessExpirationTime);
    }

    public String createRefreshToken(TokenBuildDto tokenBuildDto) {
        return createToken(tokenBuildDto, refreshExpirationTime);
    }

    private String createToken(TokenBuildDto tokenBuildDto, Long expirationTime) {
        long now = (new Date()).getTime();
        Date expiration = new Date(now + expirationTime);

        return Jwts.builder()
                .setSubject(tokenBuildDto.getSubject())
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(expiration)
                .compact();
    }

    public String getSubject(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다: {}", e.getMessage());
            throw new ApiException(ErrorEnum.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다: {}", e.getMessage());
        }
        return false;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(keyBytes);
    }
}
