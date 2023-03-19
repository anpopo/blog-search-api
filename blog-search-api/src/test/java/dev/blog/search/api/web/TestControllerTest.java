package dev.blog.search.api.web;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import dev.blog.search.common.enums.CustomResponseCode;
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