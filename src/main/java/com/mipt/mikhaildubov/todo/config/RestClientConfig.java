package com.mipt.mikhaildubov.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${external.api.base-url}")
    private String baseUrl;

    @Value("${external.api.connect-timeout:2s}")
    private Duration connectTimeout;

    @Value("${external.api.read-timeout:5s}")
    private Duration readTimeout;

    @Bean
    public RestClient externalTasksRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout((int) connectTimeout.toMillis());
        factory.setReadTimeout((int) readTimeout.toMillis());

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("User-Agent", "Todo-Gateway/1.0")
                .requestFactory(factory)
                .build();
    }
}