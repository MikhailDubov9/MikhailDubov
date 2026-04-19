package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Service class for managing tasks with caching and lifecycle logging.
 */
@Service
public class TaskService {
  private final TaskRepository repository;
  private final Map<Long, Task> taskCache = new HashMap<>();

  @Value("${app.name}")
  private String appName;

  public TaskService(TaskRepository repository) {
    this.repository = repository;
  }

  @PostConstruct
  public void initCache() {
    System.out.println("[" + appName + "] Initializing cache with tasks from repository...");
    repository.findAll().forEach(t -> taskCache.put(t.getId(), t));
  }

  @PreDestroy
  public void cleanup() {
    System.out.println("Cleaning resources. Final cache count: " + taskCache.size());
  }

  public List<Task> getAllTasks() {
    return repository.findAll();
  }

  public Optional<Task> getTaskById(Long id) {
    return repository.findById(id);
  }

  public Task saveTask(Task task) {
    return repository.save(task);
  }

  public void deleteTask(Long id) {
    repository.deleteById(id);
  }
}