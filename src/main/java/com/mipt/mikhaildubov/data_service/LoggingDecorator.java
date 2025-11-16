package com.mipt.mikhaildubov.data_service;

import java.util.Optional;

class LoggingDecorator implements DataService {

  private DataService wrapped;

  public LoggingDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    Optional<String> result = wrapped.findDataByKey(key);
    System.out.println("Data with Key: " + key + " found: " + result);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    wrapped.saveData(key, data);
    System.out.println("Data with Key: " + key + " saved: " + data);
  }

  @Override
  public boolean deleteData(String key) {
    boolean result = wrapped.deleteData(key);
    System.out.println("Data with Key: " + key + " deleted: " + result);
    return result;
  }
}