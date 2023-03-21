package dev.blog.search.api.feign;

import dev.blog.search.api.exception.ApiCommonException;
import dev.blog.search.api.feign.decoder.KakaoErrorDecoder;
import dev.blog.search.api.enums.CustomResponseCode;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Response;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KakaoErrorDecoderTest {

    @InjectMocks
    private KakaoErrorDecoder kakaoErrorDecoder;

    @DisplayName("400, 500 응답을 받으면 decode 가 ApiCommonException 을 반환한다.")
    @ParameterizedTest
    @MethodSource("provideResponseStatusAndCustomResponseCode")
    void givenBadResponseStatus_whenDecodeError_thenReturnCustomExceptions(int status, CustomResponseCode customResponseCode) {
        //given
        Response response = Response.builder()
            .status(status)
            .request(
                Request.create(HttpMethod.GET, "https://dapi.kakao.com/v2/search/blog", Collections.emptyMap(), null,
                    StandardCharsets.UTF_8, null))
            .reason("response")
            .build();

        // when
        Exception exception = kakaoErrorDecoder.decode(
            "KakaoFeignClient#getBlogPosts",
            response
        );

        // then
        Assertions.assertThat(exception).isInstanceOf(ApiCommonException.class);
        Assertions.assertThat(((ApiCommonException) exception).getCustomResponseCode()).isEqualTo(customResponseCode);
    }

    private static Stream<Arguments> provideResponseStatusAndCustomResponseCode() {
        return Stream.of(
            Arguments.of(400, CustomResponseCode.BAD_REQUEST),
            Arguments.of(401, CustomResponseCode.INTERNAL_SERVER_ERROR),
            Arguments.of(403, CustomResponseCode.BAD_REQUEST),
            Arguments.of(500, CustomResponseCode.BAD_REQUEST),
            Arguments.of(503, CustomResponseCode.BAD_REQUEST)
        );
    }


}
