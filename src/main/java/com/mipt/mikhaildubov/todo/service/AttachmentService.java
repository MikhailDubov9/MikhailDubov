package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.model.TaskAttachment;
import com.mipt.mikhaildubov.todo.repository.TaskAttachmentRepository;
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
  private final TaskAttachmentRepository repository;
  private final Path uploadDir = Paths.get("uploads");

  public AttachmentService(TaskAttachmentRepository repository) {
    this.repository = repository;
    try {
      Files.createDirectories(uploadDir);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize upload directory", e);
    }
  }

  public TaskAttachment storeAttachment(Long taskId, MultipartFile file) {
    try {
      String storedFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
      Path targetLocation = uploadDir.resolve(storedFileName);

      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

      TaskAttachment attachment = new TaskAttachment();
      attachment.setTaskId(taskId);
      attachment.setFileName(file.getOriginalFilename());
      attachment.setStoredFileName(storedFileName);
      attachment.setContentType(file.getContentType());
      attachment.setSize(file.getSize());

      return repository.save(attachment);
    } catch (IOException ex) {
      throw new RuntimeException("Could not store file " + file.getOriginalFilename(), ex);
    }
  }

  public TaskAttachment getAttachment(Long attachmentId) {
    return repository.findById(attachmentId)
        .orElseThrow(() -> new RuntimeException("Attachment not found"));
  }

  public List<TaskAttachment> getAttachmentsByTaskId(Long taskId) {
    return repository.findByTaskId(taskId);
  }

  public Resource loadAsResource(Long attachmentId) {
    try {
      TaskAttachment attachment = getAttachment(attachmentId);
      Path filePath = uploadDir.resolve(attachment.getStoredFileName()).normalize();
      Resource resource = new UrlResource(filePath.toUri());
      if (resource.exists()) {
        return resource;
      } else {
        throw new FileNotFoundException("File not found: " + attachment.getFileName());
      }
    } catch (MalformedURLException | FileNotFoundException ex) {
      throw new RuntimeException("File not found", ex);
    }
  }

  public void deleteAttachment(Long attachmentId) {
    TaskAttachment attachment = getAttachment(attachmentId);
    try {
      Files.deleteIfExists(uploadDir.resolve(attachment.getStoredFileName()));
      repository.deleteById(attachmentId);
    } catch (IOException ex) {
      throw new RuntimeException("Could not delete file", ex);
    }
  }
}