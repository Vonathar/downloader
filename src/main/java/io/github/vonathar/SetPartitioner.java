package io.github.vonathar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class SetPartitioner {

  public <T> List<Set<T>> partition(Set<T> set, int numPartitions) {

    Stack<T> items = new Stack<>();
    items.addAll(set);

    List<Set<T>> partitions = new ArrayList<>(numPartitions);

    for (int i = 0; i < numPartitions; i++) {
      partitions.add(new HashSet<>());
    }

    int i = 0;

    while (!items.empty()) {
      partitions.get(i).add(items.pop());
      ++i;
      if (i == numPartitions) {
        i = 0;
      }
    }

    return partitions;
  }
}
