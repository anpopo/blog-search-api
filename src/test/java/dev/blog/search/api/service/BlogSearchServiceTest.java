package dev.blog.search.api.service;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.client.KakaoFeignClient;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Document;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Meta;
import dev.blog.search.api.fixture.BlogFixture;
import dev.blog.search.api.web.dto.req.BlogSearchRequestDto;
import dev.blog.search.api.web.dto.res.BlogSearchRankResponseDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import java.util.List;
import java.util.Set;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BlogSearchServiceTest {

    @InjectMocks
    private BlogSearchService blogSearchService;

    @Mock
    private KakaoFeignClient kakaoFeignClient;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ZSetOperations<String, String> zOps;


    @DisplayName("검색어가 존재하지 않는 경우 예외를 발생한다.")
    @ParameterizedTest(name = "{index}번째 query 입력값 = {0}")
    @NullSource
    @ValueSource(strings = {"", " "})
    void givenEmptySearchQuery_when_thenThrowError(String value) {
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
    void givenCorrectRequestButEmptyResponseFromKakao_whenSearchBlogByCondition_thenReturnEmptyPage() {
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

    @DisplayName("올바른 검색 요청을 하는 경우 페이지 처리된 검색 결과를 반환한다.")
    @Test
    void givenCorrectRequest_whenSearchBlogByCondition_thenCorrectReturn() {
        // given
        BlogSearchRequestDto req = new BlogSearchRequestDto();
        req.setQuery("test");

        List<Document> documentListFixture = BlogFixture.getDocumentListFixture();
        Meta metaFixture = BlogFixture.getMetaFixture();

        KakaoFeignSearchResponse body = new KakaoFeignSearchResponse();
        body.setMeta(metaFixture);
        body.setDocuments(documentListFixture);

        final ResponseEntity<KakaoFeignSearchResponse> response = ResponseEntity
            .status(HttpStatus.OK)
            .body(body);

        BDDMockito.given(kakaoFeignClient.searchBlogByCondition(anyString(), anyString(), anyInt(), anyInt()))
            .willReturn(response);
        BDDMockito.given(redisTemplate.opsForZSet()).willReturn(zOps);
        BDDMockito.given(zOps.incrementScore(anyString(), anyString(), anyDouble())).willReturn(1.0);

        // when
        Page<BlogSearchResponseDto> result = blogSearchService.getBlogsByCondition(req);

        // then
        Assertions.assertThat(result.getTotalElements()).isEqualTo(metaFixture.getTotalCount());
        Assertions.assertThat(result.getContent().size()).isEqualTo(documentListFixture.size());
    }

    @DisplayName("등록된 검색어 결과가 있을 경우 검색 결과를 반환한다.")
    @Test
    void givenProperSizeOfRedisRank_whenGetBlogSearchKeywordRank_thenReturnListOfBlogSearchRank() {
        // given
        int size = 10;
        Set<TypedTuple<String>> typedTupleFixture = BlogFixture.getTypedTupleFixture();
        BDDMockito.given(redisTemplate.opsForZSet()).willReturn(zOps);
        BDDMockito.given(zOps.reverseRangeWithScores(anyString(), anyLong(), anyLong()))
            .willReturn(typedTupleFixture);

        // when
        List<BlogSearchRankResponseDto> result = blogSearchService.getBlogSearchKeywordRank(size);

        // then
        Assertions.assertThat(result.size()).isEqualTo(typedTupleFixture.size());
        Assertions.assertThat(result.get(0)).isEqualTo(new BlogSearchRankResponseDto("test3", 3.0));
    }

    @DisplayName("redis 에 검색 keyword 결과가 없는 경우 빈 리스트를 반환한다.")
    @Test
    void givenEmptyZSet_whenGetBlogSearchKeywordRank_thenReturnEmptyList() {
        // given
        int size = 10;
        BDDMockito.given(redisTemplate.opsForZSet()).willReturn(zOps);
        BDDMockito.given(zOps.reverseRangeWithScores(anyString(), anyLong(), anyLong()))
            .willReturn(null);

        // when
        List<BlogSearchRankResponseDto> result = blogSearchService.getBlogSearchKeywordRank(size);

        // then
        Assertions.assertThat(result.size()).isEqualTo(0);
    }
}
