package dev.blog.search.api.Integration;

import dev.blog.search.api.coomon.ApiCommonResponse;
import dev.blog.search.api.enums.BlogSearchSort;
import dev.blog.search.api.web.dto.res.BlogSearchRankResponseDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

class BlogSearchControllerTest extends AbstractIntegrationContainerTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @DisplayName("올바른_키워드_검색을_통해_페이지_처리_된_결과값을_받아온다")
    @Test
    void 올바른_키워드_검색을_통해_페이지_처리_된_결과값을_받아온다() {
        // given
        String url = String.format("http://localhost:%d/api/blog/search", port);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("query", "test")
            .queryParam("sort", BlogSearchSort.ACCURACY)
            .queryParam("page", 1)
            .queryParam("size", 10);

        // when
        ResponseEntity<ApiCommonResponse<Page<BlogSearchResponseDto>>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        // then
        Page<BlogSearchResponseDto> body = response.getBody().data();

        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isNotEmpty();
        Assertions.assertThat(body.getContent().get(0).getTitle()).isNotBlank();
        Assertions.assertThat(body.getTotalElements()).isGreaterThan(0);
    }

    @DisplayName("검색어 순위 조회 - 무조건 결과가 1개 이상은 나와야함")
    @Test
    void whenHaveQueryKeywordInRedis_whenGetRank_thenReturnNotEmptyList() {
        // given
        String url = String.format("http://localhost:%d/api/blog/search", port);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("query", "test")
            .queryParam("sort", BlogSearchSort.ACCURACY)
            .queryParam("page", 1)
            .queryParam("size", 10);

        restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );

        // when
        url = String.format("http://localhost:%d/api/blog/search/keyword/rank", port);
        builder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<ApiCommonResponse<List<BlogSearchRankResponseDto>>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<>() {}
        );


        // then
        List<BlogSearchRankResponseDto> body = response.getBody().data();

        Assertions.assertThat(body).isNotNull().isNotEmpty();
        Assertions.assertThat(body.get(0).getScore()).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(body.get(0).getKeyword()).isNotBlank();

    }
}