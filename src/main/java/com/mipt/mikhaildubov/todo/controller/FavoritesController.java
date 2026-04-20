package com.mipt.mikhaildubov.todo.controller;

import com.mipt.mikhaildubov.todo.dto.TaskMapper;
import com.mipt.mikhaildubov.todo.dto.TaskResponseDto;
import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorites", description = "Session-based favorite tasks")
public class FavoritesController {
  private final TaskService taskService;
  private final TaskMapper taskMapper;

  public FavoritesController(TaskService taskService, TaskMapper taskMapper) {
    this.taskService = taskService;
    this.taskMapper = taskMapper;
  }

  @SuppressWarnings("unchecked")
  private List<Long> getFavoritesFromSession(HttpSession session) {
    List<Long> favorites = (List<Long>) session.getAttribute("favoriteTaskIds");
    if (favorites == null) {
      favorites = new ArrayList<>();
      session.setAttribute("favoriteTaskIds", favorites);
    }
    return favorites;
  }

  @PostMapping("/{taskId}")
  @Operation(summary = "Add task to favorites")
  public ResponseEntity<Void> addFavorite(@PathVariable Long taskId, HttpSession session) {
    List<Long> favorites = getFavoritesFromSession(session);
    if (!favorites.contains(taskId)) {
      favorites.add(taskId);
    }
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{taskId}")
  @Operation(summary = "Remove task from favorites")
  public ResponseEntity<Void> removeFavorite(@PathVariable Long taskId, HttpSession session) {
    List<Long> favorites = getFavoritesFromSession(session);
    favorites.remove(taskId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(summary = "Get list of favorite tasks")
  public ResponseEntity<List<TaskResponseDto>> getFavorites(HttpSession session) {
    List<Long> favoriteIds = getFavoritesFromSession(session);
    List<TaskResponseDto> favoriteTasks = favoriteIds.stream()
        .map(taskService::getTaskById)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(taskMapper::toResponseDto)
        .collect(Collectors.toList());
    return ResponseEntity.ok(favoriteTasks);
  }
}