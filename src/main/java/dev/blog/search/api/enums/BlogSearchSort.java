package dev.blog.search.api.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BlogSearchSort {

    ACCURACY("accuracy", "정확도순"),
    RECENCY("recency", "최신순");

    private final String sort;
    private final String explanation;
}
