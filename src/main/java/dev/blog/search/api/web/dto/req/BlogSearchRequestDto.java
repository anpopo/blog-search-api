package dev.blog.search.api.web.dto.req;

import dev.blog.search.api.enums.BlogSearchSort;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogSearchRequestDto {
    private String query;
    // accuracy, recency
    private BlogSearchSort sort = BlogSearchSort.ACCURACY;
    private int page = 1;
    private int size = 10;

    public String getQuery() {
        return query == null ? null : query.trim();
    }

    public String getSort() {
        return this.sort.getSort();
    }
}
