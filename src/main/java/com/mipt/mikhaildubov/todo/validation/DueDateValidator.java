package com.mipt.mikhaildubov.todo.validation;

import com.mipt.mikhaildubov.todo.dto.TaskUpdateDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DueDateValidator implements ConstraintValidator<DueDateNotBeforeCreation, TaskUpdateDto> {
  @Override
  public boolean isValid(TaskUpdateDto dto, ConstraintValidatorContext context) {
    if (dto.getDueDate() == null) return true;
    return !dto.getDueDate().isBefore(LocalDate.now());
  }
}