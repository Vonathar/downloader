package io.github.vonathar.executor;

import static io.github.vonathar.testutils.FileUtils.deleteRecursively;
import static io.github.vonathar.testutils.FileUtils.getFileCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vonathar.io.FileCreator;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DownloadTaskTest {

  private final Path downloadPath = Path.of("src/test/resources/download_task_test");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(downloadPath);
  }

  @AfterEach
  public void cleanup() {
    deleteRecursively(downloadPath);
  }

  @SneakyThrows
  @Test
  public void run_ShouldDownloadFileFromTheGivenUrl() {
    Set<URI> urls = new HashSet<>();
    urls.add(URI.create("https://via.placeholder.com/1"));
    DownloadTask downloadTask = new DownloadTask(1, 5, urls, new FileCreator(downloadPath), false);
    RunnableFuture<Void> task = new FutureTask<>(downloadTask, null);
    task.run();
    assertEquals(1, getFileCount(downloadPath));
  }

  @SneakyThrows
  @Test
  public void run_ShouldSleepForAtLeastItsMinimumSleepTime() {
    Set<URI> urls = new HashSet<>();
    urls.add(URI.create("https://via.placeholder.com/1"));
    urls.add(URI.create("https://via.placeholder.com/2"));
    urls.add(URI.create("https://via.placeholder.com/3"));
    DownloadTask downloadTask =
        new DownloadTask(1000, 1001, urls, new FileCreator(downloadPath), false);
    RunnableFuture<Void> task = new FutureTask<>(downloadTask, null);

    long startTime = System.nanoTime();
    task.run();
    task.get();
    long endTime = System.nanoTime();
    long duration = (endTime - startTime) / 1000000;

    assertTrue(duration >= 3000);
  }
}
