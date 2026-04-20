package com.mipt.mikhaildubov.todo.exception;

public class TaskNotFoundException extends RuntimeException {
  public TaskNotFoundException(String message) {
    super(message);
  }
}