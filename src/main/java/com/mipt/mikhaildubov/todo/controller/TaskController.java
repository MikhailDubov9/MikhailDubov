package com.mipt.mikhaildubov.todo.controller;

import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller providing CRUD endpoints for Tasks.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> getAll() {
    return taskService.getAllTasks();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getById(@PathVariable Long id) {
    return taskService.getTaskById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public Task create(@RequestBody Task task) {
    return taskService.saveTask(task);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> update(@PathVariable Long id, @RequestBody Task task) {
    task.setId(id);
    return ResponseEntity.ok(taskService.saveTask(task));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }
}