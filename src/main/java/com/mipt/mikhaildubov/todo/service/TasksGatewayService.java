package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.client.ExternalTasksClient;
import com.mipt.mikhaildubov.todo.dto.TaskCreateDto;
import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TasksGatewayService {

    private final ExternalTasksClient client;

    public TasksGatewayService(ExternalTasksClient client) {
        this.client = client;
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "createTaskFallback")
    public ResponseEntity<TaskResponseDto> createTask(TaskCreateDto dto) {
        return client.createTask(dto);
    }

    public ResponseEntity<TaskResponseDto> createTaskFallback(TaskCreateDto dto, Throwable t) {
        TaskResponseDto fallbackDto = new TaskResponseDto();
        fallbackDto.setId(-1L);
        fallbackDto.setTitle("Fallback (Offline): " + dto.getTitle());
        return ResponseEntity.status(503).body(fallbackDto);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTaskFallback")
    public TaskResponseDto getTask(Long id) {
        return client.getTask(id);
    }

    public TaskResponseDto getTaskFallback(Long id, Throwable t) {
        TaskResponseDto fallbackDto = new TaskResponseDto();
        fallbackDto.setId(id);
        fallbackDto.setTitle("Service Unavailable");
        return fallbackDto;
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTasksFallback")
    public List<TaskResponseDto> getTasks(Boolean completed, Integer limit) {
        return client.getTasks(completed, limit);
    }

    public List<TaskResponseDto> getTasksFallback(Boolean completed, Integer limit, Throwable t) {
        return Collections.emptyList();
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteTaskFallback")
    public void deleteTask(Long id) {
        client.deleteTask(id);
    }

    public void deleteTaskFallback(Long id, Throwable t) {
    }
}