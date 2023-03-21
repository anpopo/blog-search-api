package dev.blog.search.api.service;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.client.KakaoFeignClient;
import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse;
import dev.blog.search.api.web.dto.req.BlogSearchRequestDto;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import dev.blog.search.api.enums.CustomResponseCode;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlogSearchService {

    private final KakaoFeignClient feignClient;

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

        return new PageImpl<>(
            responseBody.getDocuments().stream().map(BlogSearchResponseDto::new).collect(Collectors.toList()),
            PageRequest.of(blogSearchRequestDto.getPage(), blogSearchRequestDto.getSize()),
            responseBody.getMeta().getTotalCount()
        );
    }
}
