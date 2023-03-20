package dev.blog.search.api.web.dto.res;

import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Document;
import lombok.Getter;

@Getter
public class BlogSearchResponseDto {
    private final String title;
    private final String contents;
    private final String url;
    private final String blogName;
    private final String thumbnail;
    private final String datetime;

    public BlogSearchResponseDto(Document document) {
        this.title = document.getTitle();
        this.contents = document.getContents();
        this.url = document.getUrl();
        this.blogName = document.getBlogName();
        this.thumbnail = document.getThumbnail();
        this.datetime = document.getDatetime();
    }
}
