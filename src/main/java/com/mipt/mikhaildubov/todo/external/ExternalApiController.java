package com.mipt.mikhaildubov.todo.external;

import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import com.mipt.mikhaildubov.todo.dto.TaskCreateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

    private final Map<Long, TaskResponseDto> storage = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskCreateDto dto) {
        long id = Math.abs(random.nextLong());
        TaskResponseDto response = new TaskResponseDto();
        response.setId(id);
        response.setTitle(dto.getTitle());
        response.setCompleted(false);
        storage.put(id, response);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id) {
        if (!storage.containsKey(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(storage.get(id));
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
        storage.remove(id);
    }

    @GetMapping("/unstable")
    public ResponseEntity<?> unstable(@RequestParam String mode) throws InterruptedException {
        switch (mode) {
            case "timeout":
                Thread.sleep(6000);
                return ResponseEntity.ok().build();
            case "500":
                return ResponseEntity.internalServerError().build();
            case "429":
                return ResponseEntity.status(429).header("Retry-After", "10").build();
            default:
                return ResponseEntity.ok("Normal");
        }
    }
}