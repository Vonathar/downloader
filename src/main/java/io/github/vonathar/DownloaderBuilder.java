package io.github.vonathar;

import io.github.vonathar.exception.NoUrlsFoundException;
import io.github.vonathar.io.DirectoryCreator;
import io.github.vonathar.io.DirectoryStrategy;
import io.github.vonathar.io.UrlFileParser;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloaderBuilder {

  private final Logger log = LogManager.getLogger(DownloaderBuilder.class);

  private final Set<URI> urls = new HashSet<>();
  private Path urlFile;
  private Path outputDirectory;
  private int numThreads;
  private int minSleep;
  private int maxSleep;
  private boolean logProgress;
  private DirectoryStrategy directoryStrategy;

  public DownloaderBuilder addUrls(Set<URI> urls) {
    this.urls.addAll(urls);
    return this;
  }

  public DownloaderBuilder urlFile(Path urlFile) {
    this.urlFile = urlFile;
    return this;
  }

  public DownloaderBuilder outputDirectory(Path outputDirectory) {
    this.outputDirectory = outputDirectory;
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

  public DownloaderBuilder directoryStrategy(DirectoryStrategy directoryStrategy) {
    this.directoryStrategy = directoryStrategy;
    return this;
  }

  public Downloader build() {
    buildUrls();
    buildOutputDirectory();
    return new Downloader(urls, outputDirectory, numThreads, minSleep, maxSleep, logProgress);
  }

  private void buildUrls() {
    if (urlFile != null) {
      UrlFileParser urlFileParser = new UrlFileParser();
      urls.addAll(urlFileParser.parse(urlFile));
    }
    if (urls.isEmpty()) {
      throw new NoUrlsFoundException("Failed to find any URL to download.");
    }
    log.info("{} urls found.", urls.size());
  }

  private void buildOutputDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(directoryStrategy);
    directoryCreator.create(outputDirectory);
  }
}
