package com.audition.common.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInterceptor.class);

    /**
     * Intercepts and logs the HTTP request and response.
     *
     * <p>
     * This method is invoked when intercepting an HTTP request before it is executed and after receiving the HTTP
     * response. It logs the details of the request, including the HTTP method, URI, headers, and request body. After
     * the request is executed, it logs the response status code, headers, and response body.
     * </p>
     *
     * @param request   The intercepted {@link HttpRequest} instance representing the HTTP request.
     * @param body      The byte array containing the request body, if present.
     * @param execution The {@link ClientHttpRequestExecution} instance to execute the HTTP request.
     * @return The {@link ClientHttpResponse} received after executing the HTTP request.
     * @throws IOException if an I/O error occurs during the request execution.
     */

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        LOGGER.info("===========================request begin==============================");
        LOGGER.info("URI         : {}", request.getURI());
        LOGGER.info("Method      : {}", request.getMethod());
        LOGGER.info("Headers     : {}", request.getHeaders());
        LOGGER.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
        LOGGER.info("==========================request end================================");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        LOGGER.info("============================response begin============================");
        LOGGER.info("Status code  : {}", response.getStatusCode());
        LOGGER.info("Status text  : {}", response.getStatusText());
        LOGGER.info("Headers      : {}", response.getHeaders());
        LOGGER.info("Response body: {}", StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
        LOGGER.info("=======================response end==================================");
    }
}