package io.github.vonathar.downloader.executor;

import io.github.vonathar.downloader.exception.FileCreationException;
import io.github.vonathar.downloader.exception.HttpRequestException;
import io.github.vonathar.downloader.http.HttpClient;
import io.github.vonathar.downloader.http.SurfsharkVpn;
import io.github.vonathar.downloader.io.FileCreator;
import java.io.InputStream;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadTask implements Runnable {

  private final Logger log =
      LogManager.getLogger(DownloadTask.class);
  private final HttpClient httpClient = new HttpClient();
  private final SurfsharkVpn surfsharkVpn;
  private final int minSleep;
  private final int maxSleep;
  private final Set<URI> urls;
  private final FileCreator fileCreator;
  private final boolean logProgress;

  public DownloadTask(int minSleep, int maxSleep, Set<URI> urls,
      FileCreator fileCreator, boolean logProgress,
      String vpnPassword) {
    this.surfsharkVpn = new SurfsharkVpn(vpnPassword);
    this.minSleep = minSleep;
    this.maxSleep = maxSleep;
    this.urls = urls;
    this.fileCreator = fileCreator;
    this.logProgress = logProgress;
  }

  @Override
  public void run() {
    log.debug("Starting download thread.");
    Iterable<URI> iterables = getIterables();
    for (URI url : iterables) {
      download(url);
    }
  }

  private Iterable<URI> getIterables() {
    Iterable<URI> iterables;
    if (logProgress) {
      DownloadLogger downloadLogger = new DownloadLogger();
      iterables = downloadLogger.wrapIterable(urls.size(), urls);
    } else {
      iterables = urls;
    }
    return iterables;
  }

  private void download(URI url) {
    InputStream stream = null;
    try {
      stream = httpClient.getImage(url);
    } catch (HttpRequestException e) {
      log.error("Failed to download image from URL: {}", url, e);
      surfsharkVpn.restart();
      download(url);
    }
    createFile(stream);
    sleep();
  }

  private void createFile(InputStream stream) {
    try {
      fileCreator.create(stream);
    } catch (FileCreationException e) {
      log.error("Failed to create file.", e);
    }
  }

  private void sleep() {
    if (maxSleep == 0) {
      return;
    }
    int sleepTime =
        ThreadLocalRandom.current().nextInt(minSleep, maxSleep + 1);
    try {
      Thread.sleep(sleepTime);
    } catch (InterruptedException e) {
      log.error("Download task has been interrupted.", e);
    }
  }
}
