package com.mipt.mikhaildubov.generics;

public class Calculator<T extends Number> {

  public double sum(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() + b.doubleValue();
  }

  public double substract(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() - b.doubleValue();
  }

  public double multiply(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    return a.doubleValue() * b.doubleValue();
  }

  public double divide(T a, T b) {
    if (a == null || b == null) {
      return Double.NaN;
    }
    if (b.doubleValue() == 0) {
      return Double.NaN;
    }
    return a.doubleValue() / b.doubleValue();
  }

}
