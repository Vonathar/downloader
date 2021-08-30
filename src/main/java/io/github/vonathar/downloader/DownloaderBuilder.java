package io.github.vonathar.downloader;

import io.github.vonathar.downloader.exception.NoUrlsFoundException;
import io.github.vonathar.downloader.io.DirectoryCreator;
import io.github.vonathar.downloader.io.DirectoryStrategy;
import io.github.vonathar.downloader.io.UrlFileParser;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloaderBuilder {

  private final Logger log =
      LogManager.getLogger(DownloaderBuilder.class);

  private final Set<URI> urls = new HashSet<>();
  private Path urlFile;
  private Path downloadPath;
  private int numThreads = 1;
  private int minSleep = 0;
  private int maxSleep = 0;
  private boolean logProgress = true;
  private String vpnPassword;
  private DirectoryStrategy directoryStrategy =
      DirectoryStrategy.RENAME_CREATED;

  public DownloaderBuilder addUrls(Set<URI> urls) {
    this.urls.addAll(urls);
    return this;
  }

  public DownloaderBuilder urlFile(Path urlFile) {
    this.urlFile = urlFile;
    return this;
  }

  public DownloaderBuilder downloadPath(Path downloadPath) {
    this.downloadPath = downloadPath;
    return this;
  }

  public DownloaderBuilder numThreads(int numThreads) {
    this.numThreads = numThreads;
    return this;
  }

  public DownloaderBuilder minSleep(int minSleep) {
    this.minSleep = minSleep;
    return this;
  }

  public DownloaderBuilder maxSleep(int maxSleep) {
    this.maxSleep = maxSleep;
    return this;
  }

  public DownloaderBuilder logProgress(boolean logProgress) {
    this.logProgress = logProgress;
    return this;
  }

  public DownloaderBuilder vpnPassword(String vpnPassword) {
    this.vpnPassword = vpnPassword;
    return this;
  }

  public DownloaderBuilder directoryStrategy(
      DirectoryStrategy directoryStrategy) {
    this.directoryStrategy = directoryStrategy;
    return this;
  }

  public Downloader build() {
    buildUrls();
    buildDownloadPath();
    return new Downloader(urls, downloadPath, numThreads, minSleep,
        maxSleep, logProgress, vpnPassword);
  }

  private void buildUrls() {
    if (urlFile != null) {
      UrlFileParser urlFileParser = new UrlFileParser();
      urls.addAll(urlFileParser.parse(urlFile));
    }
    if (urls.isEmpty()) {
      throw new NoUrlsFoundException(
          "Failed to find any URL to download.");
    }
    log.info("{} urls found.", urls.size());
  }

  private void buildDownloadPath() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(directoryStrategy);
    downloadPath = directoryCreator.create(downloadPath);
  }
}
