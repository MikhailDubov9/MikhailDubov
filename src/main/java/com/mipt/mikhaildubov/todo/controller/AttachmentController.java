package com.mipt.mikhaildubov.todo.controller;

import com.mipt.mikhaildubov.todo.dto.AttachmentResponseDto;
import com.mipt.mikhaildubov.todo.model.TaskAttachment;
import com.mipt.mikhaildubov.todo.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Attachments", description = "File upload and download for tasks")
public class AttachmentController {
  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @PostMapping(value = "/api/tasks/{taskId}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload a file for a task")
  public ResponseEntity<AttachmentResponseDto> upload(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
    TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
    return ResponseEntity.ok(mapToDto(attachment));
  }

  @GetMapping("/api/tasks/{taskId}/attachments")
  @Operation(summary = "Get all attachments metadata for a task")
  public ResponseEntity<List<AttachmentResponseDto>> getTaskAttachments(@PathVariable Long taskId) {
    List<AttachmentResponseDto> dtos = attachmentService.getAttachmentsByTaskId(taskId)
        .stream().map(this::mapToDto).collect(Collectors.toList());
    return ResponseEntity.ok(dtos);
  }

  @GetMapping("/api/attachments/{attachmentId}")
  @Operation(summary = "Download file")
  public ResponseEntity<Resource> download(@PathVariable Long attachmentId) {
    TaskAttachment attachment = attachmentService.getAttachment(attachmentId);
    Resource resource = attachmentService.loadAsResource(attachmentId);

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(attachment.getContentType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getFileName() + "\"")
        .body(resource);
  }

  @DeleteMapping("/api/attachments/{attachmentId}")
  @Operation(summary = "Delete file")
  public ResponseEntity<Void> delete(@PathVariable Long attachmentId) {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent().build();
  }

  private AttachmentResponseDto mapToDto(TaskAttachment entity) {
    AttachmentResponseDto dto = new AttachmentResponseDto();
    dto.setId(entity.getId());
    dto.setFileName(entity.getFileName());
    dto.setSize(entity.getSize());
    dto.setUploadedAt(entity.getUploadedAt());
    return dto;
  }
}