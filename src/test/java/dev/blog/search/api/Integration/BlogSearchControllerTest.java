package dev.blog.search.api.Integration;

import static org.junit.jupiter.api.Assertions.*;

import dev.blog.search.api.enums.BlogSearchSort;
import dev.blog.search.api.web.dto.res.BlogSearchResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BlogSearchControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void test() {
        // given
        String url = String.format("http://localhost:%d/api/blog/search", port);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            .queryParam("query", "test")
            .queryParam("sort", BlogSearchSort.ACCURACY)
            .queryParam("page", 1)
            .queryParam("size", 10);

        // when
        ResponseEntity<Page<BlogSearchResponseDto>> response = restTemplate.exchange(
            builder.toUriString(),
            HttpMethod.GET,
            null, new ParameterizedTypeReference<>() {
            });

        // then
        Page<BlogSearchResponseDto> body = response.getBody();
        Assertions.assertThat(body).isNotNull();
        Assertions.assertThat(body.getContent()).isNotEmpty();
        Assertions.assertThat(body.getContent().get(0).getTitle()).isNotBlank();
        Assertions.assertThat(body.getTotalElements()).isGreaterThan(0);
    }
}