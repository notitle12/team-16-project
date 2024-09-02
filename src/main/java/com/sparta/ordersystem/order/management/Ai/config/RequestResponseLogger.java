package com.sparta.ordersystem.order.management.Ai.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;

public class RequestResponseLogger implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogger.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // Request 로그
        logger.info("Request URI: {}", request.getURI());
        logger.info("Request Method: {}", request.getMethod());
        logger.info("Request Headers: {}", request.getHeaders());
        logger.info("Request Body: {}", new String(body, "UTF-8"));

        ClientHttpResponse response = execution.execute(request, body);

        // Response 로그
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.info("Response Status Text: {}", response.getStatusText());
        logger.info("Response Headers: {}", response.getHeaders());

        // 응답 본문을 로깅하기 위해 BufferedReader 사용
        byte[] responseBody = response.getBody().readAllBytes();
        logger.info("Response Body: {}", new String(responseBody, "UTF-8"));

        // 응답 본문을 재설정하여 다른 코드에서 사용할 수 있도록 설정
        return new BufferingClientHttpResponseWrapper(response, responseBody);
    }
}