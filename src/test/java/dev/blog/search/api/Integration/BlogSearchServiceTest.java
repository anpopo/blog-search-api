package dev.blog.search.api.Integration;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.service.BlogSearchService;
import dev.blog.search.api.web.dto.req.BlogSearchRequestDto;
import dev.blog.search.api.web.dto.res.BlogSearchRankResponseDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class BlogSearchServiceTest extends AbstractIntegrationContainerTest {

    @Autowired
    private BlogSearchService blogSearchService;

    @DisplayName("올바른 요청으로 검색을 요청하면 검색 결과를 반환한다.")
    @Test
    void givenCorrectRequest_whenBlogSearch_thenCorrectResponse() {
        // given
        BlogSearchRequestDto blogSearchRequestDto = new BlogSearchRequestDto();
        blogSearchRequestDto.setQuery("test");

        // when
        Page<BlogSearchResponseDto> response = blogSearchService.getBlogsByCondition(blogSearchRequestDto);

        // then
        Assertions.assertThat(response).isNotNull().isNotEmpty();
        Assertions.assertThat(response.getTotalElements()).isGreaterThanOrEqualTo(0);
        Assertions.assertThat(response.getContent().size()).isGreaterThanOrEqualTo(0);
    }


    @DisplayName("올바르지 않은 요청을 하는 경우 ApiCommonException 을 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void given_when_then(String value) {
        // given
        BlogSearchRequestDto blogSearchRequestDto = new BlogSearchRequestDto();
        blogSearchRequestDto.setQuery(value);

        // when && then
        Assertions.assertThatThrownBy(() -> blogSearchService.getBlogsByCondition(blogSearchRequestDto))
            .isInstanceOf(ApiCommonException.class);
    }


    @DisplayName("rank 확인 시 redis 의 적절한 자료구조를 가져와서 반환한다.")
    @Test
    void givenSize_whenGetBlogSearchKeywordRank_thenEmptyList() {
        // given
        int size = 10;
        BlogSearchRequestDto blogSearchRequestDto = new BlogSearchRequestDto();
        blogSearchRequestDto.setQuery("test");
        blogSearchService.getBlogsByCondition(blogSearchRequestDto);


        // when
        List<BlogSearchRankResponseDto> response = blogSearchService.getBlogSearchKeywordRank(size);

        // then
        Assertions.assertThat(response).isNotNull().isNotEmpty();
    }
}
