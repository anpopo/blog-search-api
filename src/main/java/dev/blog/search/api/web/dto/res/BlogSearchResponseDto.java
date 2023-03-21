package dev.blog.search.api.web.dto.res;

import dev.blog.search.api.feign.dto.res.KakaoFeignSearchResponse.Document;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogSearchResponseDto {
    private String title;
    private String contents;
    private String url;
    private String blogName;
    private String thumbnail;
    private String datetime;

    public BlogSearchResponseDto(Document document) {
        this.title = document.getTitle();
        this.contents = document.getContents();
        this.url = document.getUrl();
        this.blogName = document.getBlogName();
        this.thumbnail = document.getThumbnail();
        this.datetime = document.getDatetime();
    }
}
