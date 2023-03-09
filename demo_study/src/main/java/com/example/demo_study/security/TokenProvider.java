package com.example.demo_study.security;

import com.example.demo_study.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//사용자 정보를 받아 JWT를 생성하는 Service

@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(UserEntity userEntity){

        //기한은 지금부터 1일로 설정
        Date expiryDate =Date.from(
                Instant.now()
                .plus(1, ChronoUnit.DAYS));

        /*
        {   //header
            "typ":"JWT", 토큰의 타입
            "alg":"HS512" 토큰의 서명을 발행하는 데 사용된 해시 알고리즘의 종류

        }.
        {   //payload
            "sub": "40288093784915d201784916c00001",  토큰의 주인을 의미
            "iss": "demo app", 토큰을 발행한 주체
            "iat" :1595733657, 토큰 발행일자
            "exp": 1595733657  토큰 종료일자
        }.
        // SECRET_KEY 를 이용해 서명한 부분
        Nn4d1MOVZg79sfACTIpCPKqwmpZMZQsNrxdJJNWkRv50_17bPLQPwnMobT4vBoG613JYjhDRKF1BSaUxzOg
         */

            //JWT TOKEN 생성
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // playload에 들어갈 내용
                .setSubject(userEntity.getId()) //sub
                .setIssuer("demo app") //iss
                .setIssuedAt(new Date()) // iat
                .setExpiration(expiryDate) //exp
        .compact();
    }

    //토큰을 디코딩 및 파싱하고, 토큰의 위조 여부를 확인
    public String validateAndGetUserId(String token){
        //parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigningKey로 넘어온 시크릿을 이용해 서명한 후, token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날힘
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.

        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token) //Base64로 Decoding and parseing
                .getBody();

        return claims.getSubject(); // 우리가 원하는 subject 즉 사용자의 아이디를 리턴.
    }
}
