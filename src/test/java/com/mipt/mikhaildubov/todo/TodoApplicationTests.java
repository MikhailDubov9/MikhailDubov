package com.mipt.mikhaildubov.todo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.mikhaildubov.todo.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.mipt.mikhaildubov.todo.service.TaskService;
import com.mipt.mikhaildubov.todo.exception.TaskUpdateException;

import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoApplicationTests {

  @Autowired
  private TaskService taskService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private String getValidTaskJson(String title) {
    return String.format("{\"title\":\"%s\",\"description\":\"Test Desc\",\"priority\":\"MEDIUM\"}", title);
  }

  @Test
  void contextLoads() {
  }

  @Test
  void shouldCreateAndRetrieveTask() throws Exception {
    mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getValidTaskJson("Test Task")))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Test Task"));

    mockMvc.perform(get("/api/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].title").value("Test Task"));
  }

  @Test
  void shouldHandleFileUpload() throws Exception {
    MvcResult result = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getValidTaskJson("File Task")))
        .andExpect(status().isCreated())
        .andReturn();

    JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
    String taskId = root.get("id").asText();

    MockMultipartFile file = new MockMultipartFile(
        "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello".getBytes()
    );

    mockMvc.perform(multipart("/api/tasks/" + taskId + "/attachments").file(file))
        .andExpect(status().is2xxSuccessful());
  }

  @Test
  void shouldTestSessionFavorites() throws Exception {
    MvcResult result = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getValidTaskJson("Fav Task")))
        .andExpect(status().isCreated())
        .andReturn();

    JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
    String taskId = root.get("id").asText();

    mockMvc.perform(post("/api/favorites/" + taskId))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/favorites"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }

  @Test
  void shouldRollbackTransactionOnFailure() throws Exception {
    MvcResult result = mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(getValidTaskJson("Task For Tx")))
        .andReturn();

    Long validId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    Long invalidId = 99999L;

    assertThrows(TaskUpdateException.class, () -> {
      taskService.bulkCompleteTasks(Arrays.asList(validId, invalidId));
    });

    Task checkTask = taskService.getTaskById(validId).get();
    assertFalse(checkTask.isCompleted());
  }
}