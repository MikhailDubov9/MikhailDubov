package com.mipt.mikhaildubov.todo.client;

import com.mipt.mikhaildubov.todo.dto.TaskCreateDto;
import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import com.mipt.mikhaildubov.todo.exception.ExternalApiException;
import com.mipt.mikhaildubov.todo.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExternalTasksClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalTasksClient.class);
    private final RestClient restClient;

    public ExternalTasksClient(RestClient externalTasksRestClient) {
        this.restClient = externalTasksRestClient;
    }

    public ResponseEntity<TaskResponseDto> createTask(TaskCreateDto dto) {
        return restClient.post()
                .uri("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(dto)
                .retrieve()
                .onStatus(status -> status.isError(), (request, response) -> handleError(response))
                .toEntity(TaskResponseDto.class);
    }

    public TaskResponseDto getTask(Long id) {
        return restClient.get()
                .uri(builder -> builder.path("/tasks/{id}").build(id))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.isError(), (request, response) -> handleError(response))
                .body(TaskResponseDto.class);
    }

    public List<TaskResponseDto> getTasks(Boolean completed, Integer limit) {
        return restClient.get()
                .uri(builder -> {
                    builder.path("/tasks");
                    if (completed != null) builder.queryParam("completed", completed);
                    if (limit != null) builder.queryParam("limit", limit);
                    return builder.build();
                })
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(status -> status.isError(), (request, response) -> handleError(response))
                .body(new ParameterizedTypeReference<>() {});
    }

    public void deleteTask(Long id) {
        restClient.delete()
                .uri(builder -> builder.path("/tasks/{id}").build(id))
                .retrieve()
                .onStatus(status -> status.isError(), (request, response) -> handleError(response))
                .toBodilessEntity();
    }

    private void handleError(ClientHttpResponse response) throws IOException {
        MediaType contentType = response.getHeaders().getContentType();
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);

        if (contentType != null && contentType.includes(MediaType.TEXT_HTML)) {
            String limitedBody = body.length() > 200 ? body.substring(0, 200) : body;
            log.error("Received HTML response instead of JSON: {}", limitedBody);
            throw new ExternalApiException("Unexpected HTML response: " + limitedBody);
        }

        if (response.getStatusCode().value() == 404) {
            throw new TaskNotFoundException("Task not found in external API: " + body);
        }

        throw new ExternalApiException("External API error: " + response.getStatusCode() + " " + body);
    }
}