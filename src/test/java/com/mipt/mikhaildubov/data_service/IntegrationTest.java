package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

class IntegrationTest {

  @Test
  void testAllDecoratorsTogether() {
    DataService complexService = new ValidationDecorator(
        new MetricableDecorator(
            new LoggingDecorator(
                new CachingDecorator(
                    new SimpleDataService()
                )
            )
        )
    );

    ByteArrayOutputStream consoleOutput = new ByteArrayOutputStream();
    System.setOut(new PrintStream(consoleOutput));

    try {
      complexService.saveData("main_key", "main_data");
      Optional<String> foundData = complexService.findDataByKey("main_key");
      complexService.deleteData("main_key");
      Optional<String> missingData = complexService.findDataByKey("main_key");

      String outputText = consoleOutput.toString();

      Assertions.assertEquals(Optional.of("main_data"), foundData);
      Assertions.assertEquals(Optional.empty(), missingData);
      Assertions.assertTrue(outputText.contains("saved: main_data"));
      Assertions.assertTrue(outputText.contains("found: Optional[main_data]"));
      Assertions.assertTrue(outputText.contains("deleted: true"));
      Assertions.assertTrue(outputText.contains("Метод выполнялся: PT"));
    } finally {
      System.setOut(System.out);
    }
  }
}