package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.exception.TaskNotFoundException;
import com.mipt.mikhaildubov.todo.exception.TaskUpdateException;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

  private final TaskRepository repository;

  public TaskService(TaskRepository repository) {
    this.repository = repository;
  }

  public List<Task> getAllTasks() {
    return repository.findAll();
  }

  public List<Task> getAllTasksWithAttachments() {
    return repository.findAllWithAttachmentsAndTags();
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

  @Transactional(rollbackFor = TaskUpdateException.class, isolation = Isolation.READ_COMMITTED)
  public void bulkCompleteTasks(List<Long> ids) {
    for (Long id : ids) {
      Task task = repository.findById(id)
          .orElseThrow(() -> new TaskUpdateException("Task not found for bulk update: " + id));
      task.setCompleted(true);
      repository.save(task);
    }
  }
}