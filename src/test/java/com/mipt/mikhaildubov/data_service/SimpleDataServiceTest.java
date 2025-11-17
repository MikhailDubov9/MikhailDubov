package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

class SimpleDataServiceTest {

  private SimpleDataService service;

  @BeforeEach
  void setup() {
    service = new SimpleDataService();
  }

  @Test
  void testSaveAndFindData() {
    service.saveData("key", "value");
    Optional<String> result = service.findDataByKey("key");
    Assertions.assertEquals(Optional.of("value"), result);
  }

  @Test
  void testDeleteData() {
    service.saveData("key", "value");
    boolean deleteResult = service.deleteData("key");
    Optional<String> afterDelete = service.findDataByKey("key");
    Assertions.assertTrue(deleteResult);
    Assertions.assertEquals(Optional.empty(), afterDelete);
  }

  @Test
  void testReturnEmptyForMissingKey() {
    Assertions.assertDoesNotThrow(() -> {
      Optional<String> result = service.findDataByKey("missing");
      Assertions.assertEquals(Optional.empty(), result);
    });
  }

  @Test
  void testOverwriteExistingData() {
    service.saveData("key", "first");
    service.saveData("key", "second");
    Optional<String> result = service.findDataByKey("key");
    Assertions.assertEquals(Optional.of("second"), result);
  }

  @Test
  void testDeleteForMissingKey() {
    boolean deleteResult = service.deleteData("nonexistent");
    Assertions.assertFalse(deleteResult);
  }
}