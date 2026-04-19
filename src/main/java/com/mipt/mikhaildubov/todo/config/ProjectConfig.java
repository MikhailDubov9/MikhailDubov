package com.mipt.mikhaildubov.todo.config;

import com.mipt.mikhaildubov.todo.repository.StubTaskRepository;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Configuration class for defining beans and enabling AOP.
 */
@Configuration
@EnableAspectJAutoProxy
public class ProjectConfig {
  @Bean
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}