package dev.blog.search.api.web.dto.res;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"keyword", "score"})
@Getter
public class BlogSearchRankResponseDto {
    private String keyword;
    private Double score;
}
