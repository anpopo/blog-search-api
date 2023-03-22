package dev.blog.search.api.feign.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class KakaoFeignSearchResponse {

    private Meta meta;
    private List<Document> documents = new ArrayList<>();

    @Setter
    @Getter
    public static class Meta {
        @JsonProperty("total_count")
        private int totalCount;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("is_end")
        private boolean isEnd;
    }

    @Setter
    @Getter
    public static class Document {
        private String title;
        private String contents;
        private String url;
        @JsonProperty("blogname")
        private String blogName;
        private String thumbnail;
        private String datetime;
    }
}
