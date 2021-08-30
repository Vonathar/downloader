package io.github.vonathar.downloader.executor;

import me.tongfei.progressbar.DelegatingProgressBarConsumer;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DownloadLogger {

  private final Logger log =
      LogManager.getLogger(DownloadLogger.class);

  public <T> Iterable<T> wrapIterable(int max,
      Iterable<T> iterable) {
    ProgressBarBuilder progressBarBuilder =
        new ProgressBarBuilder()
            .setInitialMax(max)
            .setTaskName("Downloading")
            .setConsumer(
                new DelegatingProgressBarConsumer(log::info))
            .setStyle(ProgressBarStyle.UNICODE_BLOCK)
            // TODO: Add setter parameter in builder to change interval of log.
            .setUpdateIntervalMillis(1000);
    return ProgressBar.wrap(iterable, progressBarBuilder);
  }
}
