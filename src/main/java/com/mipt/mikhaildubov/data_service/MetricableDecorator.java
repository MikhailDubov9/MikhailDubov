package com.mipt.mikhaildubov.data_service;

import java.time.Duration;
import java.util.Optional;

class MetricableDecorator implements DataService {

  private DataService wrapped;

  public MetricableDecorator(DataService wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    long startNanoTime = System.nanoTime();
    Optional<String> result = wrapped.findDataByKey(key);
    long endNanoTime = System.nanoTime();
    new MetricService().sendMetric(Duration.ofNanos(endNanoTime - startNanoTime));
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    long startNanoTime = System.nanoTime();
    wrapped.saveData(key, data);
    long endNanoTime = System.nanoTime();
    new MetricService().sendMetric(Duration.ofNanos(endNanoTime - startNanoTime));
  }

  @Override
  public boolean deleteData(String key) {
    long startNanoTime = System.nanoTime();
    boolean result = wrapped.deleteData(key);
    long endNanoTime = System.nanoTime();
    new MetricService().sendMetric(Duration.ofNanos(endNanoTime - startNanoTime));
    return result;
  }

  public static class MetricService {

    public void sendMetric(Duration duration) {
      System.out.println("Метод выполнялся: " + duration.toString());
    }
  }
}