package com.audition.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUrlLogger implements CommandLineRunner {


    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerUrlLogger.class);

    @Value("${server.port}")
    private transient String serverPort;

    @Override
    public void run(String... args) {
        String swaggerUrl = "http://localhost:" + serverPort + "/swagger-ui/index.html";
        LOGGER.info("Swagger Api Application URL : {}", swaggerUrl);
    }
}
