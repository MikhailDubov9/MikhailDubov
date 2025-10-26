package com.mipt.mikhaildubov.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom ArrayList that implements interface CustomList
 *
 * @param <A> type of elements in CustomArrayList
 */
public class CustomArrayList<A> implements CustomList<A> {

  /**
   * Default initial capacity of the list
   */
  private static final int DEFAULT_CAPACITY = 10;

  /**
   * Expansion coefficient when the list needs to grow
   */
  private static final double COEFFICIENT = 1.5;

  /**
   * Array for saving elements
   */
  private Object[] array;

  /**
   * Current number of elements in the list
   */
  private int size;

  /**
   * Current capacity of the array
   */
  private int capacity;

  /**
   * Constructs an empty list with default initial capcity
   */
  public CustomArrayList() {
    this.array = new Object[DEFAULT_CAPACITY];
    this.size = 0;
    this.capacity = DEFAULT_CAPACITY;
  }

  /**
   * Add the element to the end of the list
   *
   * @param element the element that we add to the list
   * @throws IllegalArgumentException if the element is null
   */
  @Override
  public void add(A element) {
    if (element == null) {
      throw new IllegalArgumentException("Element cannot be null");
    }

    if (size == capacity) {
      capacity = (int) (capacity * COEFFICIENT);
      Object[] newArray = new Object[capacity];
      System.arraycopy(array, 0, newArray, 0, size);
      array = newArray;
    }

    array[size] = element;
    size++;
  }

  /**
   * Get the element with index from the list
   *
   * @param index index of element that we have to return
   * @return element with index from the list
   * @throws IndexOutOfBoundsException if the index is less than 0 or greater than size - 1
   */
  @Override
  public A get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(
          "Index cannot be less than 0 and greater than size - 1 (" + (size - 1) + ")");
    }
    return (A) array[index];
  }

  /**
   * Remove the element with index from the list
   *
   * @param index index of element that we have to remove
   * @throws IndexOutOfBoundsException if the index is less than 0 or greater than size - 1
   */
  @Override
  public void remove(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(
          "Index cannot be less than 0 and greater than size - 1 (" + (size - 1) + ")");
    }
    for (int i = index; i < size - 1; i++) {
      array[i] = array[i + 1];
    }
    array[size - 1] = null;
    size--;
  }

  /**
   * Returns the number of the elements in the list
   *
   * @return the number of the elements in the list
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Checks if the list is empty
   *
   * @return true if the list is empty, otherwise returns false
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns iterator for this list
   *
   * @return iterator for this list
   */
  @Override
  public Iterator<A> iterator() {
    return new CustomArrayListIterator();
  }

  /**
   * Iterator for CustomArrayList
   */
  private class CustomArrayListIterator implements Iterator<A> {

    private int currentIndex = 0;
    private boolean canRemove = false;

    /**
     * Checks if the list has next element
     *
     * @return true if list has next element, otherwise false
     */
    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    /**
     * Returns next element from the list
     *
     * @return next element from the list
     * @throws NoSuchElementException if the list does not have next element
     */
    @Override
    public A next() {
      if (!hasNext()) {
        throw new NoSuchElementException("No more elements in this list");
      }
      canRemove = true;
      return (A) array[currentIndex++];
    }

    /**
     * Removes current element from the list
     *
     * @throws IllegalStateException if remove was called without next
     */
    @Override
    public void remove() {
      if (!canRemove) {
        throw new IllegalStateException("Can't remove from this list");
      }
      CustomArrayList.this.remove(--currentIndex);
      canRemove = false;
    }

  }
}
