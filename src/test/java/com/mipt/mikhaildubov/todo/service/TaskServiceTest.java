package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockitoBean
    private TaskRepository taskRepository;

    @Test
    public void testUpdateTaskStatus() {
        Long taskId = 1L;
        Task existingTask = new Task(taskId, "Test Task", "Description", false);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        taskService.updateTaskStatus(taskId, true);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(taskCaptor.capture());

        Task savedTask = taskCaptor.getValue();
        assertTrue(savedTask.isCompleted());
    }
}