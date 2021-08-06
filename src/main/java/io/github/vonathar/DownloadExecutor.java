package io.github.vonathar;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DownloadExecutor {

  private final ExecutorService executor;
  private final Set<DownloadTask> tasks;
  private final FileCreator fileCreator;

  public DownloadExecutor(
      int numThreads, Path downloadPath, int minSleep, int maxSleep, Set<URI> urls) {
    this.executor = Executors.newFixedThreadPool(numThreads);
    this.fileCreator = new FileCreator(downloadPath);
    SetPartitioner partitioner = new SetPartitioner();
    List<Set<URI>> partitions = partitioner.partition(urls, numThreads);

    this.tasks =
        (partitions.stream()
            .map(partition -> new DownloadTask(minSleep, maxSleep, partition, fileCreator))
            .collect(Collectors.toSet()));
  }

  public void start() {
    for (DownloadTask task : tasks) {
      executor.submit(task);
    }
    executor.shutdown();
    try {
      executor.awaitTermination(5, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
