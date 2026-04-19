package com.mipt.mikhaildubov.todo.repository;

import com.mipt.mikhaildubov.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Stub implementation of TaskRepository with hardcoded values for testing.
 */
public class StubTaskRepository implements TaskRepository {
  @Override
  public List<Task> findAll() {
    return List.of(new Task(999L, "Stub Task", "Stub Description", false));
  }

  @Override
  public Optional<Task> findById(Long id) {
    return Optional.empty();
  }

  @Override
  public Task save(Task task) {
    return task;
  }

  @Override
  public void deleteById(Long id) {
  }
}