package dev.blog.search.api.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@Slf4j
public class KakaoRequestInterceptor implements RequestInterceptor {

    @Value("${feign.api.key.kakao}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate template) {
        log.info("[KakaoRequestInterceptor] request path to : {}", template.path());
        template.header("Authorization", String.format("KakaoAK %s", apiKey));
    }
}
