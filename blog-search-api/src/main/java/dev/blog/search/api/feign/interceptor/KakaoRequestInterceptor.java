package dev.blog.search.api.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class KakaoRequestInterceptor implements RequestInterceptor {

    @Value("${feign.api.key.kakao}")
    private String apiKey;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", String.format("KakaoAK %s", apiKey));
    }
}
