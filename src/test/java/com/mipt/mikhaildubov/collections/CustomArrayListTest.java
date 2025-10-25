package com.mipt.mikhaildubov.collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class CustomArrayListTest {

  @Test
  void testAddandGetandSize() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");
    assertEquals(3, list.size());
    assertEquals("A", list.get(0));
    assertEquals("B", list.get(1));
  }

  @Test
  public void testAddWithCapacityExpansion() {
    CustomList<String> list = new CustomArrayList<>();
    for (int i = 0; i < 25; ++i) {
      list.add(Integer.toString(i));
    }

    assertEquals(25, list.size());
    for (int i = 0; i < 25; ++i) {
      assertEquals(Integer.toString(i), list.get(i));
    }
  }

  @Test
  void testAddNull() {
    CustomList<String> list = new CustomArrayList<>();
    assertThrows(IllegalArgumentException.class, () -> list.add(null));
  }

  @Test
  void testGetInvalidIndex() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("A");
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
  }

  @Test
  void testRemove() {
    CustomList<Integer> list = new CustomArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);

    list.remove(1);
    assertEquals(2, list.size());
    assertEquals(1, list.get(0));
    assertEquals(3, list.get(1));
  }

  @Test
  void testRemoveInvalidIndex() {
    CustomList<String> list = new CustomArrayList<>();
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    list.add("Test");
    assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
  }

  @Test
  void testisEmpty() {
    CustomList<String> list = new CustomArrayList<>();
    assertTrue(list.isEmpty());
    list.add("A");
    list.add("B");
    list.add("C");
    assertFalse(list.isEmpty());
  }

  @Test
  void testIteratorhasNextandNext() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");

    Iterator<String> iterator = list.iterator();
    assertTrue(iterator.hasNext());
    assertEquals("A", iterator.next());
    assertEquals("B", iterator.next());
    assertEquals("C", iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  void testIteratorRemove() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("A");
    list.add("B");
    list.add("C");

    Iterator<String> iterator = list.iterator();
    iterator.next();
    iterator.next();
    iterator.remove();

    assertEquals(2, list.size());
    assertEquals("A", list.get(0));
    assertEquals("C", list.get(1));
  }

  @Test
  void testIteratorRemoveBeforeNext() {
    CustomList<String> list = new CustomArrayList<>();
    list.add("A");
    Iterator<String> iterator = list.iterator();

    assertThrows(IllegalStateException.class, () -> iterator.remove());
  }

  @Test
  void testIteratorNextWhenNoElementsLeft() {
    CustomList<String> list = new CustomArrayList<>();
    Iterator<String> iterator = list.iterator();
    assertThrows(NoSuchElementException.class, () -> iterator.next());
  }
}