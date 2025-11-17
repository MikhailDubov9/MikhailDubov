package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

class ValidationDecoratorTest {

  private SimpleDataService realService;
  private ValidationDecorator validationService;

  @BeforeEach
  void setup() {
    realService = new SimpleDataService();
    validationService = new ValidationDecorator(realService);
  }

  @Test
  void testThrowsExceptionForNullKeyInFind() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.findDataByKey(null);
    });
    Assertions.assertEquals("Key cannot be null or blank", exception.getMessage());
  }

  @Test
  void testThrowsExceptionForEmptyKeyInFind() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.findDataByKey("   ");
    });
    Assertions.assertEquals("Key cannot be null or blank", exception.getMessage());
  }

  @Test
  void testThrowsExceptionForNullKeyInSave() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData(null, "good_data");
    });
    Assertions.assertEquals("Key cannot be null or blank", exception.getMessage());
  }

  @Test
  void testThrowsExceptionForEmptyKeyInSave() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("   ", "good_data");
    });
    Assertions.assertEquals("Key cannot be null or blank", exception.getMessage());
  }

  @Test
  void testThrowsExceptionForNullDataInSave() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("good_key", null);
    });
    Assertions.assertEquals("Data cannot be null or blank", exception.getMessage());
  }

  @Test
  void testThrowsExceptionForEmptyDataInSave() {
    Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
      validationService.saveData("good_key", "   ");
    });
    Assertions.assertEquals("Data cannot be null or blank", exception.getMessage());
  }

  @Test
  void testWorksCorrectly() {
    validationService.saveData("good_key", "good_data");
    Optional<String> foundData = validationService.findDataByKey("good_key");
    boolean deleteResult = validationService.deleteData("good_key");
    Assertions.assertEquals(Optional.of("good_data"), foundData);
    Assertions.assertTrue(deleteResult);
  }
}