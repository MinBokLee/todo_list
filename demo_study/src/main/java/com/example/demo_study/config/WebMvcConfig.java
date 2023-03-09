package com.example.demo_study.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 경로에 대해
        registry.addMapping("/**")  //프로그램에서 제공하는 URL
                //Origin이 http:localhost:3000에 대해
                .allowedOrigins("*")  // 청을 허용할 출처를 명시, 전체 허용 (가능하다면 목록을 작성한다.
                //GET, POST, PUT, PATH, DELETE,OPTIONS 메서드를 허용한다.
                .allowedMethods("*")  // 어떤 헤더들을 허용할 것인지
                .allowedHeaders("*") // 어떤 메서드를 허용할 것인지 (GET, POST...)
                .allowCredentials(false) // 쿠키 요청을 허용한다(다른 도메인 서버에 인증하는 경우에만 사용해야하며, true 설정시 보안상 이슈가 발생할 수 있다)
                .maxAge(MAX_AGE_SECS); //preflight 요청에 대한 응답을 브라우저에서 캐싱하는 시간 ;
    }
}
