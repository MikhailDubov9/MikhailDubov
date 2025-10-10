package com.mipt.mikhaildubov.generics;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

  public static <T> List<T> mergeLists(List<? extends T> list1,
      List<? extends T> list2) {
    List<T> result = new ArrayList<>();
    if (list1 != null) {
      result.addAll(list1);
    }
    if (list2 != null) {
      result.addAll(list2);
    }
    return result;
  }

  public static <T> void addAll(List<? super T> destination,
      List<? extends T> source) {
    if (destination == null || source == null) {
      return;
    }
    destination.addAll(source);
  }

}
