package com.mipt.mikhaildubov.todo.repository;

import com.mipt.mikhaildubov.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Interface for task data access operations.
 */
public interface TaskRepository {
  List<Task> findAll();

  Optional<Task> findById(Long id);

  Task save(Task task);

  void deleteById(Long id);
}