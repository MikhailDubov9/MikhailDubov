package com.mipt.mikhaildubov.collections;

/**
 *Interface for CustomList
 *
 * @param <A> type of elements in CustomArrayList
 */

public interface CustomList<A> extends Iterable<A> {

  /**
   * Add the element to the end of the list
   *
   * @param element the element that we add to the list
   * @throws IllegalArgumentException if the element is null
   */
  void add(A element);

  /**
   * Get the element with index from the list
   *
   * @param index index of element that we have to return
   * @return element with index from the list
   * @throws IndexOutOfBoundsException if the index is less than 0 or greater than size - 1
   */
  A get(int index);

  /**
   * Remove the element with index from the list
   *
   * @param index index of element that we have to remove
   * @throws IndexOutOfBoundsException if the index is less than 0 or greater than size - 1
   */
  void remove(int index);

  /**
   * Returns the number of the elements in the list
   *
   * @return the number of the elements in the list
   */
  int size();

  /**
   * Checks if the list is empty
   *
   * @return true if the list is empty, otherwise returns false
   */
  boolean isEmpty();
}
