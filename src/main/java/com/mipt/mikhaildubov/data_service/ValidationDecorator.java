package com.mipt.mikhaildubov.data_service;

import java.util.Optional;

class ValidationDecorator implements DataService {

  private DataService wrapped;

  public ValidationDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Key cannot be null or blank");
    }
    return wrapped.findDataByKey(key);
  }

  @Override
  public void saveData(String key, String data) {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Key cannot be null or blank");
    }
    if (data == null || data.isBlank()) {
      throw new IllegalArgumentException("Data cannot be null or blank");
    }
    wrapped.saveData(key, data);
  }

  @Override
  public boolean deleteData(String key) {
    if (key == null || key.isBlank()) {
      throw new IllegalArgumentException("Key cannot be null or blank");
    }
    return wrapped.deleteData(key);
  }
}