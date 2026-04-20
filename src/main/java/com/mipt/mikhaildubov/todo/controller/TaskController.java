package com.mipt.mikhaildubov.todo.controller;

import com.mipt.mikhaildubov.todo.dto.TaskCreateDto;
import com.mipt.mikhaildubov.todo.dto.TaskMapper;
import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import com.mipt.mikhaildubov.todo.dto.TaskUpdateDto;
import com.mipt.mikhaildubov.todo.exception.TaskNotFoundException;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.service.TaskService;
import com.mipt.mikhaildubov.todo.validation.OnCreate;
import com.mipt.mikhaildubov.todo.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "To-Do List API")
public class TaskController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  @Value("${app.version:2.0.0}")
  private String apiVersion;

  public TaskController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @GetMapping
  @Operation(summary = "Get all tasks")
  public ResponseEntity<List<TaskResponseDto>> getAll() {
    List<Task> tasks = taskService.getAllTasks();
    List<TaskResponseDto> response = tasks.stream().map(taskMapper::toResponseDto).collect(Collectors.toList());

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Total-Count", String.valueOf(response.size()));
    headers.add("X-API-Version", apiVersion);

    return ResponseEntity.ok().headers(headers).body(response);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get task by ID")
  public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
    Task task = taskService.getTaskById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(taskMapper.toResponseDto(task));
  }

  @PostMapping
  @Operation(summary = "Create a new task")
  public ResponseEntity<TaskResponseDto> create(@Validated(OnCreate.class) @RequestBody TaskCreateDto dto) {
    Task task = taskMapper.toEntity(dto);
    task.setId(System.currentTimeMillis());
    Task savedTask = taskService.saveTask(task);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("X-API-Version", apiVersion)
        .body(taskMapper.toResponseDto(savedTask));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a task")
  public ResponseEntity<TaskResponseDto> update(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TaskUpdateDto dto) {
    Task existingTask = taskService.getTaskById(id)
        .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));

    taskMapper.updateEntity(dto, existingTask);
    Task updatedTask = taskService.saveTask(existingTask);
    return ResponseEntity.ok()
        .header("X-API-Version", apiVersion)
        .body(taskMapper.toResponseDto(updatedTask));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete a task")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    if (taskService.getTaskById(id).isEmpty()) {
      throw new TaskNotFoundException("Task with id " + id + " not found");
    }
    taskService.deleteTask(id);
    return ResponseEntity.noContent().header("X-API-Version", apiVersion).build();
  }
}