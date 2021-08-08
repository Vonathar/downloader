package io.github.vonathar;

import io.github.vonathar.io.UrlFileParser;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class DownloaderBuilder {

  private Set<URI> urls;
  private Path urlFile;
  private Path outputDirectory;
  private int numThreads;
  private int minSleep;
  private int maxSleep;

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

  public Downloader build() {
    Set<URI> urls = buildUrls();
    return new Downloader(urls, outputDirectory, numThreads, minSleep, maxSleep);
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
    return urls;
  }
}
