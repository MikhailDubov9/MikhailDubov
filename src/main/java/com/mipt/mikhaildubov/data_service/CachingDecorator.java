package com.mipt.mikhaildubov.data_service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class CachingDecorator implements DataService {

  private DataService wrapped;
  private Map<String, Optional<String>> cache = new HashMap<>();

  public CachingDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (cache.containsKey(key)) {
      return cache.get(key);
    }

    Optional<String> result = wrapped.findDataByKey(key);
    cache.put(key, result);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    wrapped.saveData(key, data);
    cache.put(key, Optional.of(data));
  }

  @Override
  public boolean deleteData(String key) {
    boolean result = wrapped.deleteData(key);
    if (result) {
      cache.remove(key);
    }
    return result;
  }
}