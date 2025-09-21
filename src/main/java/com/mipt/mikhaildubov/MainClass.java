package com.mipt.mikhaildubov;

public class MainClass {
  private int justInt;
  private String justString;
  protected static double justDouble;
  public final long justLong = 52;

  public static void main(String[] args) {
    for (int i = 0; i < 15; i++) {
      System.out.println("Iter: " + i);
    }
  }
}
