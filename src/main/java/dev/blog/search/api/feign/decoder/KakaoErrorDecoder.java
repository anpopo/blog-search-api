package dev.blog.search.api.feign.decoder;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.enums.CustomResponseCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
@RequiredArgsConstructor
public class KakaoErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus httpStatus = HttpStatus.resolve(response.status());

        if (httpStatus == HttpStatus.UNAUTHORIZED) {
            log.error("Kakao API Error invalid api key: {} {}", httpStatus.value(), httpStatus.getReasonPhrase());
            return new ApiCommonException(CustomResponseCode.INTERNAL_SERVER_ERROR, "Api key is invalid");
        } else if (httpStatus != null && httpStatus.isError()) {
            log.error("Kakao API Error: {} {}", httpStatus.value(), httpStatus.getReasonPhrase());
            return new ApiCommonException(CustomResponseCode.BAD_REQUEST, httpStatus.getReasonPhrase());
        }

        return new ApiCommonException(CustomResponseCode.INTERNAL_SERVER_ERROR, "Unexpected error occurred.");
    }
}
