package dev.blog.search.api.coomon;

import dev.blog.search.api.enums.CustomResponseCode;
import java.io.Serializable;

public record ApiCommonResponse<T> (T data, String customCode, String message) implements Serializable {

    public static <T> ApiCommonResponse<T> createError(final String customCode, final String message) {
        return new ApiCommonResponse<>(null, customCode, message);
    }

    public static <T> ApiCommonResponse<T> createOK(final T data) {
        final CustomResponseCode success = CustomResponseCode.SUCCESS;
        return new ApiCommonResponse<>(data, success.getCustomCode(), success.getDefaultMessage());
    }
}
