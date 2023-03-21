package dev.blog.search.api.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.client.KakaoFeignClient;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse;
import dev.blog.search.api.web.dto.req.BlogSearchRequestDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {

    @InjectMocks
    private BlogSearchService blogSearchService;

    @Mock
    private KakaoFeignClient kakaoFeignClient;

    @DisplayName("검색어가 존재하지 않는 경우 예외를 발생한다.")
    @ParameterizedTest(name = "{index}번째 query 입력값 = {0}")
    @NullSource
    @ValueSource(strings = {"", " "})
    void givenEmptySearchQuery_when_then(String value) {
        // given
        BlogSearchRequestDto req = new BlogSearchRequestDto();
        req.setQuery(value);

        // when && then
        Assertions.assertThatThrownBy(() -> blogSearchService.getBlogsByCondition(req))
            .isInstanceOf(ApiCommonException.class)
            .hasMessage("검색어를 입력해주세요.");
    }

    @DisplayName("response 의 body 에 필요 데이터가 없는 경우 빈 페이지 객체를 반환한다.")
    @Test
    void given_when_then() {
        // given
        BlogSearchRequestDto req = new BlogSearchRequestDto();
        req.setQuery("test");

        final ResponseEntity<KakaoFeignSearchResponse> response = ResponseEntity
            .status(HttpStatus.OK)
            .body(new KakaoFeignSearchResponse());

        BDDMockito.given(kakaoFeignClient.searchBlogByCondition(anyString(), anyString(), anyInt(), anyInt()))
            .willReturn(response);

        // when
        Page<BlogSearchResponseDto> result = blogSearchService.getBlogsByCondition(req);

        // then
        Assertions.assertThat(result).isEmpty();
    }
}