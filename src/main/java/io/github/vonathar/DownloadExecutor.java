package io.github.vonathar;

import io.github.vonathar.io.FileCreator;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadExecutor {

  private final Logger log = LogManager.getLogger(DownloadExecutor.class);

  private final ExecutorService executor;
  private final Set<DownloadTask> tasks;
  private final FileCreator fileCreator;

  public DownloadExecutor(
      Set<URI> urls,
      Path downloadPath,
      int numThreads,
      int minSleep,
      int maxSleep,
      boolean logProgress) {
    log.debug("Creating executor with {} threads.", numThreads);
    this.executor = Executors.newFixedThreadPool(numThreads);
    this.fileCreator = new FileCreator(downloadPath);
    SetPartitioner partitioner = new SetPartitioner();
    List<Set<URI>> partitions = partitioner.partition(urls, numThreads);
    this.tasks =
        (partitions.stream()
            .map(
                partition ->
                    new DownloadTask(minSleep, maxSleep, partition, fileCreator, logProgress))
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
