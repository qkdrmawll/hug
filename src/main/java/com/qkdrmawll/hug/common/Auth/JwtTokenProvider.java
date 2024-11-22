package com.qkdrmawll.hug.common.Auth;

import com.qkdrmawll.hug.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.secretKeyRt}")
    private String secretKeyRT;
    @Value("${jwt.expirationRt}")
    private int expirationRt;

    public String createToken(String email, List<String> roles) {
//        사용자 정보이자 페이로드 정보
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간
                .setExpiration(new Date(now.getTime() + expiration*60*1000L)) // 만료시간 30분
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        return token;
    }

    public String createRefreshToken(String email, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now) // 생성시간
                .setExpiration(new Date(now.getTime() + expirationRt*60*1000L)) // 만료시간 30분
                .signWith(SignatureAlgorithm.HS256,secretKeyRT)
                .compact();
        return token;
    }
}
