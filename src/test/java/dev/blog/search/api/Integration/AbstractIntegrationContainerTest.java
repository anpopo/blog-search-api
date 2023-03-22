package dev.blog.search.api.Integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractIntegrationContainerTest {

    static final GenericContainer REDIS_CONTAINER;

    static {
        REDIS_CONTAINER
            = new GenericContainer<>(DockerImageName.parse("redis:7.0.10-alpine"))
                .withExposedPorts(6379);

        REDIS_CONTAINER.start();

        System.setProperty("spring.data.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.data.redis.port", REDIS_CONTAINER.getFirstMappedPort().toString());
    }
}
