package com.example.demo_study.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    // 동일패키지에 속하는 클래스와 하위클래스 관계에서 접근 가능 protected
    //2.TokenProvider를 이용해 토큰을 인증하고, UsernamePasswordAuthenticationToken을 작성한다.
    //  이 오브젝트의 사용자의 인증 정보를 저장하고, SecurityContext에 인증된 사용자를 등록한다.
    // 왜 등록해야 하는가? 요청을 처리하는 과정에서 사용자가 인증됐는지의 여부나 인증된 사용자가 누군지 알아야 할 때가 있기 때문이다.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
                                                            , FilterChain filterChain) throws ServletException, IOException {
        try {
            //리퀘스트에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running");

            //토큰 검사하기. JWT 이므로, 인가 서버에 요청하지 않고도 검증 가능
            if(token != null && !token.equalsIgnoreCase("null")){

                //userId 가져오기, 위조된 경우 예외 처리된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID :" + userId);

                // 인증 완료. SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,
                                                                                            null,
                                                                                                     AuthorityUtils.NO_AUTHORITIES);
                // 인증된 사용자의 정보 . 문자열이 아니어도 아무것이나 넣을 수 있다. 보통 UserDetails라는 오브젝트를 넣는데 우리는 넣지 않았다.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SpringSecurity의 SecurityContext는 SeturityContextHolder의 createEmptyContext() 메서드를 이용해 생성할 수 있다.
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                // 생성한 context에 인증 정보인 authentication을 넣고
                securityContext.setAuthentication(authentication);
                // 다시 SecurityContextHolder에 컨텍스트로 등록.
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception ex) {
            logger.error("Cloud not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }
    // SecurityContextHolder 는 기본적 으로 ThreadLocal 에 저장 된다. ThreadLocal에 저장되므로,
    // Thread 마다 하나의 컨텍스트를 관리할 수 있으며 같은 Thread 내라면, 어디에서든 접근할 수 있다.

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 헤더를 파싱해 Bearer 토큰을 리턴 한다.
        log.info("request", request);
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            log.info("bearerToken---->>>" , bearerToken);
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
