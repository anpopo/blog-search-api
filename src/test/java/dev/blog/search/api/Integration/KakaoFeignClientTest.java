package dev.blog.search.api.Integration;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.client.KakaoFeignClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class KakaoFeignClientTest {

    @Autowired
    private KakaoFeignClient kakaoFeignClient;

    @DisplayName("feign 클라이언트의 연동 규격에 맞지 않는 요청을 하면 ApiCommonException 이 발생한다.")
    @Test
    void given_whenKakaoBlogSearchApiSendErrorResponse_then() {
        // when && then
        Assertions.assertThatThrownBy(() -> kakaoFeignClient.searchBlogByCondition(null, "accuracy", 1, 1))
                .isInstanceOf(ApiCommonException.class);

    }

}
