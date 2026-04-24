package com.mipt.mikhaildubov.todo.exception;

public class TaskUpdateException extends RuntimeException {
  public TaskUpdateException(String message) {
    super(message);
  }
}