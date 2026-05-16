package com.mipt.mikhaildubov.todo.controller;

import com.mipt.mikhaildubov.todo.dto.TaskCreateDto;
import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import com.mipt.mikhaildubov.todo.service.TasksGatewayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TasksGatewayController {

    private final TasksGatewayService gatewayService;

    public TasksGatewayController(TasksGatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskCreateDto dto) {
        return gatewayService.createTask(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(gatewayService.getTask(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(gatewayService.getTasks(completed, limit));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        gatewayService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}