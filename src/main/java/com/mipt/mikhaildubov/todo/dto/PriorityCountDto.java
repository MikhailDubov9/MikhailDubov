package com.mipt.mikhaildubov.todo.dto;

public class PriorityCountDto {
  private String priority;
  private int count;

  public PriorityCountDto(String priority, int count) {
    this.priority = priority;
    this.count = count;
  }

  public String getPriority() {
    return priority;
  }

  public int getCount() {
    return count;
  }
}