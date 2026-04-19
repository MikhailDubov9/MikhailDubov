package com.mipt.mikhaildubov.todo.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

/**
 * Bean with request scope to track unique HTTP requests.
 */
@Component
@RequestScope
public class RequestScopedBean {
  private final String requestId = UUID.randomUUID().toString();
  private final long startTime = System.currentTimeMillis();

  public String getRequestId() {
    return requestId;
  }

  public long getStartTime() {
    return startTime;
  }
}