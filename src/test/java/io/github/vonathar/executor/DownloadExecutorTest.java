package io.github.vonathar.executor;

import static io.github.vonathar.testutils.FileUtils.deleteRecursively;
import static io.github.vonathar.testutils.FileUtils.getFileCount;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DownloadExecutorTest {

  private final Path downloadPath = Path.of("src/test/resources/executor_test");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(downloadPath);
  }

  @AfterEach
  public void cleanup() {
    deleteRecursively(downloadPath);
  }

  @Test
  public void start_SingleThread_SingleUrl_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(1);
    int numThreads = 1;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }

  private Set<URI> getTestUrls(int n) {
    Set<URI> urls = new HashSet<>();
    for (int i = 0; i < n; i++) {
      urls.add(URI.create("https://via.placeholder.com/" + i));
    }
    return urls;
  }

  @Test
  public void start_SingleThread_SmallUrlSet_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(3);
    int numThreads = 1;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }

  @Test
  public void start_SingleThread_LargeUrlSet_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(10);
    int numThreads = 1;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }

  @Test
  public void start_MultipleThreads_SmallUrlSet_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(4);
    int numThreads = 4;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }

  @Test
  public void start_MultipleThreads_LargeUrlSet_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(40);
    int numThreads = 4;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }

  @Test
  public void start_MultipleThreads_MoreThreadsThanUrls_ShouldDownloadTheGivenUrls() {
    Set<URI> urls = getTestUrls(3);
    int numThreads = 6;
    int minSleep = 50;
    int maxSleep = 100;
    DownloadExecutor executor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, false);

    executor.start();

    int expected = urls.size();
    int downloaded = getFileCount(downloadPath);
    assertEquals(expected, downloaded);
  }
}
