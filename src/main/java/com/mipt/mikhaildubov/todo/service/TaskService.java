package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.exception.TaskUpdateException;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository repository;
    private final Map<Long, Task> taskCache = new HashMap<>();

    @Value("${app.name:TodoApp}")
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

    public Task updateTaskStatus(Long id, boolean completed) {
        Task task = repository.findById(id)
                .orElseThrow(() -> new TaskUpdateException("Task not found for bulk update: " + id));
        task.setCompleted(completed);
        return repository.save(task);
    }

    @Transactional(rollbackFor = TaskUpdateException.class, isolation = Isolation.READ_COMMITTED)
    public void bulkCompleteTasks(List<Long> ids) {
        for (Long id : ids) {
            updateTaskStatus(id, true);
        }
    }
}