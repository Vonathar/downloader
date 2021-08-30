package io.github.vonathar.downloader.io;

import io.github.vonathar.downloader.exception.FileParsingException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UrlFileParser {

  private final Logger log =
      LogManager.getLogger(UrlFileParser.class);

  public Set<URI> parse(Path file) {
    Set<URI> urls = new HashSet<>();

    try (Stream<String> stream = Files.lines(file)) {
      for (var line : stream.toArray(String[]::new)) {
        try {
          urls.add(new URL(line).toURI());
        } catch (MalformedURLException | URISyntaxException e) {
          log.error("Failed to parse URL: '{}'", line);
        }
      }
    } catch (IOException e) {
      throw new FileParsingException(file, e);
    }
    return urls;
  }
}
