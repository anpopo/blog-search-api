package dev.blog.search.api.exception;

import dev.blog.search.api.enums.CustomResponseCode;
import lombok.Getter;

@Getter
public class ApiCommonException extends RuntimeException {
    private final CustomResponseCode customResponseCode;

    public ApiCommonException(CustomResponseCode customResponseCode) {
        this(customResponseCode, customResponseCode.getDefaultMessage());
    }

    public ApiCommonException(CustomResponseCode customResponseCode, String message) {
        super(message);
        this.customResponseCode = customResponseCode;
    }
}
