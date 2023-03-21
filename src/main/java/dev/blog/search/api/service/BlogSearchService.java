package dev.blog.search.api.service;

import static dev.blog.search.api.coomon.BlogSearchConstant.REDIS_SEARCH_QUERY_KEY;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.client.KakaoFeignClient;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse;
import dev.blog.search.api.web.dto.req.BlogSearchRequestDto;
import dev.blog.search.api.web.dto.res.BlogSearchRankResponseDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import dev.blog.search.api.enums.CustomResponseCode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogSearchService {

    private final KakaoFeignClient feignClient;
    private final RedisTemplate<String, String> redisTemplate;

    public Page<BlogSearchResponseDto> getBlogsByCondition(BlogSearchRequestDto blogSearchRequestDto) {

        if (blogSearchRequestDto.getQuery() == null || blogSearchRequestDto.getQuery().isEmpty()) {
            throw new ApiCommonException(CustomResponseCode.BAD_REQUEST, "검색어를 입력해주세요.");
        }

        ResponseEntity<KakaoFeignSearchResponse> response = feignClient.searchBlogByCondition(
            blogSearchRequestDto.getQuery(),
            blogSearchRequestDto.getSort(),
            blogSearchRequestDto.getSize(),
            blogSearchRequestDto.getPage());

        KakaoFeignSearchResponse responseBody = response.getBody();

        if (responseBody == null || responseBody.getDocuments() == null || responseBody.getDocuments().isEmpty()) {
            return Page.empty();
        }

        redisTemplate.opsForZSet().incrementScore(REDIS_SEARCH_QUERY_KEY, blogSearchRequestDto.getQuery(), 1);

        return new PageImpl<>(
            responseBody.getDocuments().stream().map(BlogSearchResponseDto::new).collect(Collectors.toList()),
            PageRequest.of(blogSearchRequestDto.getPage(), blogSearchRequestDto.getSize()),
            responseBody.getMeta().getTotalCount()
        );
    }

    public List<BlogSearchRankResponseDto> getBlogSearchKeywordRank(int size) {
        size = validatePageSize(size);

        ZSetOperations<String, String> zOps = redisTemplate.opsForZSet();
        Set<TypedTuple<String>> rank = zOps.reverseRangeWithScores(REDIS_SEARCH_QUERY_KEY, 0, size);

        if (rank == null) {
            return Collections.emptyList();
        }

        return rank.stream()
            .map(t -> new BlogSearchRankResponseDto(t.getValue(), t.getScore()))
            .collect(Collectors.toList());
    }

    private int validatePageSize(int size) {
        if (size < 10) {
            size = 10;
        } else if (size > 1000) {
            size = 1000;
        }
        return size;
    }
}
