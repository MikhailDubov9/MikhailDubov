package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Service to demonstrate @Qualifier usage by injecting multiple repository implementations.
 */
@Service
public class TaskStatisticsService {
  private final TaskRepository primaryRepo;
  private final TaskRepository stubRepo;

  public TaskStatisticsService(TaskRepository primaryRepo,
                               @Qualifier("stubTaskRepository") TaskRepository stubRepo) {
    this.primaryRepo = primaryRepo;
    this.stubRepo = stubRepo;
  }

  public void printComparison() {
    System.out.println("Primary Repo count: " + primaryRepo.findAll().size());
    System.out.println("Stub Repo count: " + stubRepo.findAll().size());
  }
}