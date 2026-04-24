package com.mipt.mikhaildubov.todo.repository;

import com.mipt.mikhaildubov.todo.model.Priority;
import com.mipt.mikhaildubov.todo.model.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByCompletedAndPriority(boolean completed, Priority priority);

  @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN CURRENT_DATE AND :nextWeek")
  List<Task> findTasksDueIn7Days(@Param("nextWeek") LocalDate nextWeek);

  @EntityGraph(attributePaths = {"attachments", "tags"})
  @Query("SELECT t FROM Task t")
  List<Task> findAllWithAttachmentsAndTags();
}