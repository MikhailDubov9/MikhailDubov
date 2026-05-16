package com.mipt.mikhaildubov.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTask() throws Exception {
        Task taskToSave = new Task();
        taskToSave.setTitle("New Web Task");
        taskToSave.setDescription("Desc");
        taskToSave.setCompleted(false);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("New Web Task");
        savedTask.setDescription("Desc");
        savedTask.setCompleted(false);

        when(taskService.saveTask(any(Task.class))).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskToSave)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Web Task"));
    }

    @Test
    public void testGetTask() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(2L);
        existingTask.setTitle("Existing Web Task");
        existingTask.setDescription("Desc");
        existingTask.setCompleted(true);

        when(taskService.getTaskById(2L)).thenReturn(Optional.of(existingTask));

        mockMvc.perform(get("/api/tasks/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Existing Web Task"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}