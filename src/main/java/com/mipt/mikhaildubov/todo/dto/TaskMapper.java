package com.mipt.mikhaildubov.todo.dto;

import com.mipt.mikhaildubov.todo.model.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  Task toEntity(TaskCreateDto dto);

  TaskResponseDto toResponseDto(Task task);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);
}