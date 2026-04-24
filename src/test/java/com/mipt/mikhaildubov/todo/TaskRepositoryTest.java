package com.mipt.mikhaildubov.todo;

import com.mipt.mikhaildubov.todo.model.Priority;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TaskRepositoryTest {

  @Autowired
  private TaskRepository taskRepository;

  @Test
  public void testCustomQueryDueIn7Days() {
    Task taskDueSoon = new Task();
    taskDueSoon.setTitle("Due Soon");
    taskDueSoon.setPriority(Priority.HIGH);
    taskDueSoon.setDueDate(LocalDate.now().plusDays(3));
    taskRepository.save(taskDueSoon);

    Task taskDueLate = new Task();
    taskDueLate.setTitle("Due Late");
    taskDueLate.setPriority(Priority.LOW);
    taskDueLate.setDueDate(LocalDate.now().plusDays(10));
    taskRepository.save(taskDueLate);

    List<Task> results = taskRepository.findTasksDueIn7Days(LocalDate.now().plusDays(7));

    assertThat(results).hasSize(1);
    assertThat(results.get(0).getTitle()).isEqualTo("Due Soon");
  }
}