package com.mipt.mikhaildubov.todo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@Tag(name = "Preferences", description = "Cookie-based user preferences")
public class PreferencesController {

  @GetMapping("/view")
  @Operation(summary = "Get view preference from cookie")
  public ResponseEntity<String> getViewPreference(
      @CookieValue(value = "viewPreference", defaultValue = "detailed") String viewPreference) {
    return ResponseEntity.ok("Current view mode: " + viewPreference);
  }

  @PostMapping("/view")
  @Operation(summary = "Set view preference cookie")
  public ResponseEntity<String> setViewPreference(
      @RequestParam(defaultValue = "detailed") String mode,
      HttpServletResponse response) {

    Cookie cookie = new Cookie("viewPreference", mode);
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60);
    response.addCookie(cookie);

    return ResponseEntity.ok("View mode saved: " + mode);
  }
}