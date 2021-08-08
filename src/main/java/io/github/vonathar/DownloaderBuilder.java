package io.github.vonathar;

import io.github.vonathar.exception.NoUrlsFoundException;
import io.github.vonathar.io.UrlFileParser;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloaderBuilder {

  private final Logger log = LogManager.getLogger(DownloaderBuilder.class);

  private Set<URI> urls;
  private Path urlFile;
  private Path outputDirectory;
  private int numThreads;
  private int minSleep;
  private int maxSleep;
  private boolean logProgress;

  public DownloaderBuilder setUrls(Set<URI> urls) {
    this.urls = urls;
    return this;
  }

  public DownloaderBuilder setUrlFile(Path urlFile) {
    this.urlFile = urlFile;
    return this;
  }

  public DownloaderBuilder setOutputDirectory(Path outputDirectory) {
    this.outputDirectory = outputDirectory;
    return this;
  }

  public DownloaderBuilder setNumThreads(int numThreads) {
    this.numThreads = numThreads;
    return this;
  }

  public DownloaderBuilder setMinSleep(int minSleep) {
    this.minSleep = minSleep;
    return this;
  }

  public DownloaderBuilder setMaxSleep(int maxSleep) {
    this.maxSleep = maxSleep;
    return this;
  }

  public DownloaderBuilder setLogProgress(boolean logProgress) {
    this.logProgress = logProgress;
    return this;
  }

  public Downloader build() {
    Set<URI> urls = buildUrls();
    return new Downloader(urls, outputDirectory, numThreads, minSleep, maxSleep, logProgress);
  }

  private Set<URI> buildUrls() {
    Set<URI> urls = new HashSet<>();
    if (this.urls != null) {
      urls.addAll(this.urls);
    }
    if (this.urlFile != null) {
      UrlFileParser urlFileParser = new UrlFileParser();
      urls.addAll(urlFileParser.parse(this.urlFile));
    }
    if (urls.isEmpty()) {
      throw new NoUrlsFoundException("Failed to find any URL to download.");
    }

    log.info("{} urls found.", urls.size());
    return urls;
  }
}
