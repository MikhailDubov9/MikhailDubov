package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

class MetricableDecoratorTest {

  private SimpleDataService realService;
  private MetricableDecorator metricsService;
  private ByteArrayOutputStream testOutput;

  @BeforeEach
  void setup() {
    realService = new SimpleDataService();
    metricsService = new MetricableDecorator(realService);
    testOutput = new ByteArrayOutputStream();
    System.setOut(new PrintStream(testOutput));
  }

  @Test
  void testShowsTimeForFind() {
    realService.saveData("key", "data");
    Optional<String> result = metricsService.findDataByKey("key");
    String outputText = testOutput.toString();
    Assertions.assertEquals(Optional.of("data"), result);
    Assertions.assertTrue(outputText.contains("Метод выполнялся: PT"));
  }

  @Test
  void testShowsTimeForSave() {
    metricsService.saveData("new_key", "new_value");
    String outputText = testOutput.toString();
    Assertions.assertTrue(outputText.contains("Метод выполнялся: PT"));
    Optional<String> savedData = realService.findDataByKey("new_key");
    Assertions.assertEquals(Optional.of("new_value"), savedData);
  }

  @Test
  void testShowsTimeForDelete() {
    realService.saveData("delete_key", "data");
    boolean deleteResult = metricsService.deleteData("delete_key");
    String outputText = testOutput.toString();
    Assertions.assertTrue(deleteResult);
    Assertions.assertTrue(outputText.contains("Метод выполнялся: PT"));
  }
}