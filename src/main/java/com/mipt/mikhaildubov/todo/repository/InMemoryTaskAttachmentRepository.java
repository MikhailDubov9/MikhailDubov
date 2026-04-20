package com.mipt.mikhaildubov.todo.repository;

import com.mipt.mikhaildubov.todo.model.TaskAttachment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskAttachmentRepository implements TaskAttachmentRepository {
  private final Map<Long, TaskAttachment> attachments = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public TaskAttachment save(TaskAttachment attachment) {
    if (attachment.getId() == null) {
      attachment.setId(idGenerator.getAndIncrement());
    }
    attachments.put(attachment.getId(), attachment);
    return attachment;
  }

  @Override
  public Optional<TaskAttachment> findById(Long id) {
    return Optional.ofNullable(attachments.get(id));
  }

  @Override
  public List<TaskAttachment> findByTaskId(Long taskId) {
    List<TaskAttachment> result = new ArrayList<>();
    for (TaskAttachment attachment : attachments.values()) {
      if (attachment.getTaskId().equals(taskId)) {
        result.add(attachment);
      }
    }
    return result;
  }

  @Override
  public void deleteById(Long id) {
    attachments.remove(id);
  }
}