package com.mipt.mikhaildubov.todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Main class to run the Spring Boot application.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class TodoApplication {
  public static void main(String[] args) {
    SpringApplication.run(TodoApplication.class, args);
  }
}