package dev.blog.search.api.feign.config;

import dev.blog.search.api.feign.interceptor.KakaoRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoFeignConfig {

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return new KakaoRequestInterceptor();
    }
}
