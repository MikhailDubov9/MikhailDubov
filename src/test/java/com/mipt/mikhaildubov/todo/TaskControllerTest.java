package com.mipt.mikhaildubov.todo;

import com.mipt.mikhaildubov.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for TaskController CRUD endpoints.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void testCreateTask_Positive() {
    Task task = new Task(1L, "Test", "Desc", false);
    ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", task, Task.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Test", response.getBody().getTitle());
  }

  @Test
  void testGetTask_Negative() {
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/999", Task.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void testGetAll_Positive() {
    restTemplate.postForEntity("/api/tasks", new Task(2L, "T2", "D2", true), Task.class);
    ResponseEntity<Task[]> response = restTemplate.getForEntity("/api/tasks", Task[].class);
    assertTrue(response.getBody().length > 0);
  }

  @Test
  void testUpdateTask_Positive() {
    Task t = new Task(3L, "Old", "Desc", false);
    restTemplate.postForEntity("/api/tasks", t, Task.class);
    t.setTitle("New");
    restTemplate.put("/api/tasks/3", t);
    Task updated = restTemplate.getForObject("/api/tasks/3", Task.class);
    assertEquals("New", updated.getTitle());
  }

  @Test
  void testDelete_Positive() {
    restTemplate.postForEntity("/api/tasks", new Task(4L, "Del", "D", false), Task.class);
    restTemplate.delete("/api/tasks/4");
    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/4", Task.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}