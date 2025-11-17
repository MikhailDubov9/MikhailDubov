package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class LoggingDecoratorTest {

  private SimpleDataService realService;
  private LoggingDecorator loggingService;
  private ByteArrayOutputStream testOutput;

  @BeforeEach
  void setup() {
    realService = new SimpleDataService();
    loggingService = new LoggingDecorator(realService);
    testOutput = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOutput));
  }

  @Test
  void testLogFind() {
    realService.saveData("test_key", "test_data");
    loggingService.findDataByKey("test_key");
    String logText = testOutput.toString();
    Assertions.assertTrue(logText.contains("Data with Key: test_key found: Optional[test_data]"));
  }

  @Test
  void testLogSave() {
    loggingService.saveData("save_key", "save_value");
    String logText = testOutput.toString();
    Assertions.assertTrue(logText.contains("Data with Key: save_key saved: save_value"));
  }

  @Test
  void testLogDelete() {
    realService.saveData("delete_key", "data");
    loggingService.deleteData("delete_key");
    String logText = testOutput.toString();
    Assertions.assertTrue(logText.contains("Data with Key: delete_key deleted: true"));
  }

  @Test
  void testLogNotFound() {
    loggingService.findDataByKey("missing_key");
    String logText = testOutput.toString();
    Assertions.assertTrue(logText.contains("Data with Key: missing_key found: Optional.empty"));
  }
}