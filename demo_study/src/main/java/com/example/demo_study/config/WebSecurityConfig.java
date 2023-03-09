package com.example.demo_study.config;

import com.example.demo_study.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String[] DOCS_URI = {
            "/swagger",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api-docs",
            "/api-docs/**",
            "/v3/api-docs/**"
    };


    @Autowired
    private  JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //HTTP Security Builder
        http.cors() // WebMvConfig애서 이미 설정했으므로 기본 cors 설정
            .and()
            .csrf() // csrf는 현재 사용하지 않으므로 disable
                .disable()
             .httpBasic() //token을 사용하므로 basic 인증 disable
                .disable()
             .sessionManagement() //session 기반이 아님을 설정
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
             .and()
             .authorizeRequests() // /와 /auth/** 경로는 인증 안해도 됨.
                .antMatchers("/", "/auth/**").permitAll()
                .antMatchers(DOCS_URI).permitAll()
              .anyRequest()  //  /와 /auth/** 이외의 모든 경로는 인증이 되어야 함.
                .authenticated();
            //filter 등록, 매 요청 마다, CorsFilter 실행한 후에 jwtAuthenticationFilter 실행 함.
              http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
    }

    /* HTTPSecuritysms 시큐리티 설정을 위한 오브젝트.
        이 오브젝트는 빌더를 제공하는데 빌더를 이용해, cors, csrf, httpBasic, session, authroizeRequest 등
        다양한 설정을 할 수 있다.
         web.xml 대신 HTTPSecurity를 이용해 시규리티 관련설정을 한다.
     */
}