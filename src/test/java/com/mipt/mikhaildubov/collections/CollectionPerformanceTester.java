package com.mipt.mikhaildubov.collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CollectionPerformanceTester {

  private static final int NUMBER_OF_ELEMENTS = 10000;

  @Test
  public void testPerfomance() {
    System.out.println("Performance Comparison of ArrayList and LinkedList");
    System.out.println("====================================================");
    System.out.printf("%-20s %-15s %-15s%n", "Operation", "ArrayList (ms)", "LinkedList (ms)");
    System.out.println("----------------------------------------------------");

    testAddToEnd();
    testAddToStart();
    testInsertInMiddle();
    testAccessByIndex();
    testRemoveFromStart();
    testRemoveFromEnd();

    System.out.println("====================================================");
  }

  private void testAddToEnd() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.add(i);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.add(i);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Add to End", arrayListTime, linkedListTime);
  }

  private void testAddToStart() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.add(0, i);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.add(0, i);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Add to Start", arrayListTime, linkedListTime);
  }

  private void testInsertInMiddle() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      int middle = arrayList.size() / 2;
      arrayList.add(middle, i);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      int middle = linkedList.size() / 2;
      linkedList.add(middle, i);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Insert in Middle", arrayListTime, linkedListTime);
  }

  private void testAccessByIndex() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.get(i);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.get(i);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Access by Index", arrayListTime, linkedListTime);
  }

  private void testRemoveFromStart() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.remove(0);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.remove(0);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Remove from Start", arrayListTime, linkedListTime);
  }

  private void testRemoveFromEnd() {
    List<Integer> arrayList = new ArrayList<>();
    long startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      arrayList.remove(arrayList.size() - 1);
    }
    long arrayListTime = (System.nanoTime() - startTime) / 1_000_000;

    List<Integer> linkedList = new LinkedList<>();
    startTime = System.nanoTime();
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.add(i);
    }
    for (int i = 0; i < NUMBER_OF_ELEMENTS; i++) {
      linkedList.remove(linkedList.size() - 1);
    }
    long linkedListTime = (System.nanoTime() - startTime) / 1_000_000;

    System.out.printf("%-25s %-15d %-15d%n", "Remove from End", arrayListTime, linkedListTime);
  }
}
