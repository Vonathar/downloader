package io.github.vonathar.downloader.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SetPartitionerTest {

  private final SetPartitioner partitioner = new SetPartitioner();

  @Test
  void partition_ShouldSuccessfullyPartitionSingleElementSets() {
    HashSet<String> set = new HashSet<>(List.of("1", "2", "3"));
    List<Set<String>> output = partitioner.partition(set, 3);
    HashSet<String> set1 = new HashSet<>(List.of("1"));
    HashSet<String> set2 = new HashSet<>(List.of("2"));
    HashSet<String> set3 = new HashSet<>(List.of("3"));

    assertTrue(output.containsAll(List.of(set1, set2, set3)));
  }

  @Test
  void
  partition_ShouldSuccessfullyPartitionForAnyNumberOfPartitions() {
    HashSet<String> set = new HashSet<>(
        List.of("1", "2", "3", "4", "5", "6", "7", "8"));

    List<Set<String>> output = partitioner.partition(set, 2);
    HashSet<String> set1 =
        new HashSet<>(List.of("1", "3", "5", "7"));
    HashSet<String> set2 =
        new HashSet<>(List.of("2", "4", "6", "8"));

    assertTrue(output.containsAll(List.of(set1, set2)));
  }

  @Test
  void partition_ShouldSuccessfullyPartitionMultiElementSets() {
    HashSet<String> set = new HashSet<>(
        List.of("1", "2", "3", "4", "5", "6", "7", "8", "9"));

    List<Set<String>> output = partitioner.partition(set, 3);
    HashSet<String> set1 = new HashSet<>(List.of("1", "4", "7"));
    HashSet<String> set2 = new HashSet<>(List.of("2", "5", "8"));
    HashSet<String> set3 = new HashSet<>(List.of("3", "6", "9"));

    assertTrue(output.containsAll(List.of(set1, set2, set3)));
  }

  @Test
  void
  partition_ShouldSuccessfullyPartitionSetsWithUnevenElements() {
    HashSet<String> set =
        new HashSet<>(List.of("1", "2", "3", "4", "5"));

    List<Set<String>> output = partitioner.partition(set, 2);
    HashSet<String> set1 = new HashSet<>(List.of("1", "3", "5"));
    HashSet<String> set2 = new HashSet<>(List.of("2", "4"));

    assertTrue(output.containsAll(List.of(set1, set2)));
  }

  @Test
  void partition_ShouldSuccessfullyPartitionLargeSets() {
    HashSet<String> set = new HashSet<>();
    for (char c = 0; c < 10000; c++) {
      set.add(String.valueOf(c));
    }

    List<Set<String>> output = partitioner.partition(set, 10);
    for (Set<String> partition : output) {
      assertEquals(1000, partition.size());
    }
  }
}
