package dev.blog.search.api.web.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogSearchRequestDto {
    private String query;
    private String sort = "accuracy";
    private int page = 1;
    private int size = 10;

    public String getQuery() {
        return query == null ? null : query.trim();
    }
}
