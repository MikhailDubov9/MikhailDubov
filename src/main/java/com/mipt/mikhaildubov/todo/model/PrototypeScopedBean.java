package com.mipt.mikhaildubov.todo.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Bean with prototype scope for generating unique IDs.
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {
  public String generateUniqueId() {
    return UUID.randomUUID().toString();
  }
}