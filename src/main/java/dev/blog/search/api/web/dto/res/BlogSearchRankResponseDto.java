package dev.blog.search.api.web.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BlogSearchRankResponseDto {
    private String keyword;
    private Double size;
}
