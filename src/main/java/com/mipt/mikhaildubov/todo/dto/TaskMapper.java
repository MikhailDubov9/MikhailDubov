package com.mipt.mikhaildubov.todo.dto;

import com.mipt.mikhaildubov.todo.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastModifiedDate", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  Task toEntity(TaskCreateDto dto);

  TaskResponseDto toResponseDto(Task task);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  Task updateEntity(TaskUpdateDto dto, @MappingTarget Task task);
}