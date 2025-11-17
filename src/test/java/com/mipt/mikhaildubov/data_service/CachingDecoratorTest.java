package com.mipt.mikhaildubov.data_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

class CachingDecoratorTest {

  private SimpleDataService realService;
  private CachingDecorator cachingService;

  @BeforeEach
  void setup() {
    realService = new SimpleDataService();
    cachingService = new CachingDecorator(realService);
  }

  @Test
  void testReturnsCachedValue() {
    cachingService.saveData("key1", "value1");
    realService.saveData("key1", "changed_value");
    Optional<String> result = cachingService.findDataByKey("key1");
    Assertions.assertEquals(Optional.of("value1"), result);
  }

  @Test
  void testUpdatesCache() {
    cachingService.saveData("key", "first_value");
    cachingService.saveData("key", "second_value");
    Optional<String> result = cachingService.findDataByKey("key");
    Assertions.assertEquals(Optional.of("second_value"), result);
  }

  @Test
  void testRemovesFromCache() {
    cachingService.saveData("key", "data");
    Optional<String> beforeDelete = cachingService.findDataByKey("key");
    Assertions.assertEquals(Optional.of("data"), beforeDelete);
    boolean deleteResult = cachingService.deleteData("key");
    Assertions.assertTrue(deleteResult);
    Optional<String> afterDelete = cachingService.findDataByKey("key");
    Assertions.assertEquals(Optional.empty(), afterDelete);
  }

  @Test
  void testCachesEmptyResults() {
    Optional<String> firstResult = cachingService.findDataByKey("missing");
    Assertions.assertEquals(Optional.empty(), firstResult);
    cachingService.saveData("missing", "now_exists");
    Optional<String> secondResult = cachingService.findDataByKey("missing");
    Assertions.assertEquals(Optional.of("now_exists"), secondResult);
  }
}