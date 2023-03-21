package dev.blog.search.api.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.blog.search.api.enums.CustomResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TestController.class)
class TestControllerTest {

    @Autowired
    private MockMvc mvc;

    @DisplayName("예외 발생하지 않을 경우 정상 200 응답을 반환한다.")
    @Test
    void health() throws Exception {
        // when && then
        mvc.perform(
                MockMvcRequestBuilders.get("/test/health")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("예외 발생할 경우 ApiCommonResponse 형태로 응답을 반환한다.")
    @Test
    void unhealthy() throws Exception {
        // when && then
        final CustomResponseCode customResponseCode = CustomResponseCode.BAD_REQUEST;
        mvc.perform(
                MockMvcRequestBuilders.get("/test/unhealthy")
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                jsonPath("$.customCode").value(customResponseCode.getCustomCode()),
                jsonPath("$.message").value(customResponseCode.getDefaultMessage())
            );
    }
}