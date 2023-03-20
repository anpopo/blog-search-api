package dev.blog.search.api.feign.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
public class KakaoFeignSearchRequest {

    private String query;
    private String sort = "accuracy";
    private int page = 1;
    private int size = 10;
}
