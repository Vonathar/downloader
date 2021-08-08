package io.github.vonathar.exception;

import java.nio.file.Path;

public class FileParsingException extends RuntimeException {

  public FileParsingException(Path path, Throwable cause) {
    super("Failed to parse file: " + path, cause);
  }
}