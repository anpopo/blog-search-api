package dev.blog.search.api.web;

import dev.blog.search.api.enums.CustomResponseCode;
import dev.blog.search.api.exception.ApiCommonException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/health")
    public Long health() {
        return 1L;
    }

    @GetMapping("/unhealthy")
    public String unhealthy() {
        throw new ApiCommonException(CustomResponseCode.BAD_REQUEST);
    }

}
