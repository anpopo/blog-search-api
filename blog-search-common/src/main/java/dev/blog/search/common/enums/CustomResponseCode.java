package dev.blog.search.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomResponseCode {
    SUCCESS("S200", 200, "Success"),
    BAD_REQUEST("E400", 400, "Bad Request"),
    NOT_FOUND("E404", 404, "Not Found"),
    INTERNAL_SERVER_ERROR("E500", 500, "Internal Server Error"),
    ;

    private final String customCode;
    private final int statusCode;
    private final String defaultMessage;
}
