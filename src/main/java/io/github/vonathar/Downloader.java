package io.github.vonathar;

import java.net.URI;
import java.nio.file.Path;
import java.util.Set;

public class Downloader {
  private final Set<URI> urls;
  private final Path downloadPath;
  private final int numThreads;
  private final int minSleep;
  private final int maxSleep;
  private final boolean logProgress;

  public Downloader(
      Set<URI> urls,
      Path downloadPath,
      int numThreads,
      int minSleep,
      int maxSleep,
      boolean logProgress) {
    this.urls = urls;
    this.downloadPath = downloadPath;
    this.numThreads = numThreads;
    this.minSleep = minSleep;
    this.maxSleep = maxSleep;
    this.logProgress = logProgress;
  }

  public void start() {
    DownloadExecutor downloadExecutor =
        new DownloadExecutor(urls, downloadPath, numThreads, minSleep, maxSleep, logProgress);
  }
}
