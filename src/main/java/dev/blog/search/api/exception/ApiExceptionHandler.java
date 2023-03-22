package dev.blog.search.api.exception;

import dev.blog.search.api.coomon.ApiCommonResponse;
import dev.blog.search.api.enums.CustomResponseCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiCommonException.class)
    public ApiCommonResponse<Void> handleApiCommonException(final ApiCommonException e, final HttpServletResponse res) {
        final String customCode = e.getCustomResponseCode().getCustomCode();
        final String message = e.getMessage();

        log.error("ApiCommonException - code: {}, message: {}", customCode, message);
        res.setStatus(e.getCustomResponseCode().getStatusCode());
        return ApiCommonResponse.createError(customCode, message);
    }

    @ExceptionHandler(Exception.class)
    public ApiCommonResponse<Void> handleException(final Exception e, final HttpServletResponse res) {
        final String message = e.getMessage();
        final CustomResponseCode customErrorCode = CustomResponseCode.INTERNAL_SERVER_ERROR;

        log.error("Exception - message: {}", message);
        res.setStatus(customErrorCode.getStatusCode());
        return ApiCommonResponse.createError(customErrorCode.getCustomCode(), customErrorCode.getDefaultMessage());
    }
}
