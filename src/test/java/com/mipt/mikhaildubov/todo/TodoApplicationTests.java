package com.mipt.mikhaildubov.todo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.mikhaildubov.todo.exception.TaskUpdateException;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.security.JwtUtils;
import com.mipt.mikhaildubov.todo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskService taskService;

    @Autowired
    private JwtUtils jwtUtils;

    private String token;

    @BeforeEach
    void setUp() {
        token = "Bearer " + jwtUtils.generateToken("user");
    }

    private String getValidTaskJson(String title) {
        return String.format("{\"title\":\"%s\",\"description\":\"Test Desc\",\"priority\":\"MEDIUM\"}", title);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void shouldCreateAndRetrieveTask() throws Exception {
        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidTaskJson("Test Task")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"));

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }

    @Test
    void shouldHandleFileUpload() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidTaskJson("File Task")))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        String taskId = root.get("id").asText();

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello".getBytes()
        );

        mockMvc.perform(multipart("/api/tasks/" + taskId + "/attachments")
                        .file(file)
                        .header("Authorization", token))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void shouldTestSessionFavorites() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getValidTaskJson("Fav Task")))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        String taskId = root.get("id").asText();

        mockMvc.perform(post("/api/favorites/" + taskId)
                        .header("Authorization", token))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/favorites")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void shouldRollbackTransactionOnFailure() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .header("Authorization", token)
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