package dev.blog.search.api.feign.client;

import dev.blog.search.api.feign.config.KakaoFeignConfig;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoFeignClient",
    url = "${feign.url.prefix}",
    configuration = KakaoFeignConfig.class)
public interface KakaoFeignClient {

    @GetMapping(value = "/v2/search/blog")
    ResponseEntity<KakaoFeignSearchResponse> searchBlogByCondition(
        @RequestParam("query") String query,
        @RequestParam("sort") String sort,
        @RequestParam("size") int size,
        @RequestParam("page") int page);
}
