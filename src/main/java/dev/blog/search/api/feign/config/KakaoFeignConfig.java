package dev.blog.search.api.feign.config;

import dev.blog.search.api.feign.decoder.KakaoErrorDecoder;
import dev.blog.search.api.feign.interceptor.KakaoRequestInterceptor;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;


public class KakaoFeignConfig {

    @Bean
    public RequestInterceptor kakaoRequestInterceptor() {
        return new KakaoRequestInterceptor();
    }

    @Bean
    public ErrorDecoder kakaoErrorDecoder() {
        return new KakaoErrorDecoder();
    }
}
