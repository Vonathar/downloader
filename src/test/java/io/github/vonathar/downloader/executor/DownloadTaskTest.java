package io.github.vonathar.downloader.executor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vonathar.downloader.io.FileCreator;
import io.github.vonathar.downloader.testutils.FileUtils;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DownloadTaskTest {

  private final Path downloadPath =
      Path.of("src/test/resources/download_task_test");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(downloadPath);
  }

  @AfterEach
  public void cleanup() {
    FileUtils.deleteRecursively(downloadPath);
  }

  @SneakyThrows
  @Test
  public void run_ShouldDownloadFileFromTheGivenUrl() {
    Set<URI> urls = new HashSet<>();
    urls.add(URI.create("https://via.placeholder.com/1"));
    DownloadTask downloadTask = new DownloadTask(
        1, 5, urls, new FileCreator(downloadPath), false, "");
    RunnableFuture<Void> task = new FutureTask<>(downloadTask, null);
    task.run();
    Assertions.assertEquals(1, FileUtils.getFileCount(downloadPath));
  }

  @SneakyThrows
  @Test
  public void run_ShouldSleepForAtLeastItsMinimumSleepTime() {
    Set<URI> urls = new HashSet<>();
    urls.add(URI.create("https://via.placeholder.com/1"));
    urls.add(URI.create("https://via.placeholder.com/2"));
    urls.add(URI.create("https://via.placeholder.com/3"));
    DownloadTask downloadTask = new DownloadTask(
        1000, 1001, urls, new FileCreator(downloadPath), false, "");
    RunnableFuture<Void> task = new FutureTask<>(downloadTask, null);

    long startTime = System.nanoTime();
    task.run();
    task.get();
    long endTime = System.nanoTime();
    long duration = (endTime - startTime) / 1000000;

    assertTrue(duration >= 3000);
  }
}
