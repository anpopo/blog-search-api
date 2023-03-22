package dev.blog.search.api.config;

import dev.blog.search.api.enums.BlogSearchSort;
import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class BlogSearchSortConverter implements Converter<String, BlogSearchSort> {
    @Override
    public BlogSearchSort convert(String source) {
        if (validSort(source)) {
            return BlogSearchSort.valueOf(source.toUpperCase());
        }

        return BlogSearchSort.ACCURACY;
    }

    private boolean validSort(final String source) {
        return StringUtils.hasText(source) && Arrays.stream(BlogSearchSort.values())
            .anyMatch(blogSearchSort -> blogSearchSort.name().equalsIgnoreCase(source));
    }
}