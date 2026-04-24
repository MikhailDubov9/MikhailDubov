package com.mipt.mikhaildubov.todo.service;

import com.mipt.mikhaildubov.todo.dto.PriorityCountDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatisticsService {

  private final JdbcTemplate jdbcTemplate;

  public TaskStatisticsService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<PriorityCountDto> getTasksCountByPriority() {
    String sql = "SELECT priority, count(*) as count FROM tasks GROUP BY priority";

    return jdbcTemplate.query(sql, (rs, rowNum) ->
        new PriorityCountDto(
            rs.getString("priority"),
            rs.getInt("count")
        )
    );
  }
}