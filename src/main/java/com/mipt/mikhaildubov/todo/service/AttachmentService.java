package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.model.Task;
import com.mipt.mikhaildubov.todo.model.TaskAttachment;
import com.mipt.mikhaildubov.todo.repository.TaskAttachmentRepository;
import com.mipt.mikhaildubov.todo.repository.TaskRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {

  private final TaskAttachmentRepository attachmentRepository;
  private final TaskRepository taskRepository;
  private final Path uploadDir = Paths.get("uploads");

  public AttachmentService(TaskAttachmentRepository attachmentRepository, TaskRepository taskRepository) {
    this.attachmentRepository = attachmentRepository;
    this.taskRepository = taskRepository;
    try {
      Files.createDirectories(uploadDir);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize upload directory", e);
    }
  }

  public TaskAttachment storeAttachment(Long taskId, MultipartFile file) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new RuntimeException("Task not found"));

    try {
      String storedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
      Path targetLocation = uploadDir.resolve(storedFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      TaskAttachment attachment = new TaskAttachment();
      attachment.setTask(task); // Связываем сущности!
      attachment.setFileName(file.getOriginalFilename());
      attachment.setStoredFileName(storedFileName);
      attachment.setContentType(file.getContentType());
      attachment.setSize(file.getSize());

      return attachmentRepository.save(attachment);
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file", ex);
    }
  }

  public TaskAttachment getAttachment(Long attachmentId) {
    return attachmentRepository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
  }

  public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {
    return attachmentRepository.findByTask_Id(taskId);
  }

  public Resource loadAsResource(Long attachmentId) {
    try {
      TaskAttachment attachment = getAttachment(attachmentId);
      Path filePath = uploadDir.resolve(attachment.getStoredFileName()).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found");
      }
    } catch (MalformedURLException | FileNotFoundException ex) {
      throw new RuntimeException("File not found", ex);
    }
  }

  public void deleteAttachment(Long attachmentId) {
    TaskAttachment attachment = getAttachment(attachmentId);
    try {
      Files.deleteIfExists(uploadDir.resolve(attachment.getStoredFileName()));
      attachmentRepository.deleteById(attachmentId);
    } catch (IOException ex) {
      throw new RuntimeException("Could not delete file", ex);
    }
  }
}