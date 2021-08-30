package io.github.vonathar.downloader.exception;

import java.nio.file.Path;

public class FileCreationException extends Exception {

  public FileCreationException(Path path, Throwable cause) {
    super(String.format("Failed to create file: %s", path), cause);
  }
}
