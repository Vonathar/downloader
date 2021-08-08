package io.github.vonathar.io;

import io.github.vonathar.exception.DirectoryCreationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

public class DirectoryCreator {

  private final DirectoryStrategy directoryStrategy;

  public DirectoryCreator(DirectoryStrategy directoryStrategy) {
    this.directoryStrategy = directoryStrategy;
  }

  public Path create(Path directory) {
    try {
      if (Files.notExists(directory)) {
        Files.createDirectories(directory);
        return directory;
      }
      switch (directoryStrategy) {
        case DELETE_EXISTING:
          delete(directory);
          Files.createDirectories(directory);
          return directory;
        case RENAME_CREATED:
          Path alternative = getAlternative(directory);
          Files.createDirectories(alternative);
          return alternative;
        case REUSE_EXISTING:
        default:
          return directory;
      }
    } catch (IOException e) {
      throw new DirectoryCreationException(
          String.format("Failed to create directory: %s", directory), e);
    }
  }

  private void delete(Path directory) {
    try {
      Files.walk(directory)
          .sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
    } catch (IOException e) {
      throw new DirectoryCreationException(
          String.format("Failed to delete existing directory: %s", directory), e);
    }
  }

  private Path getAlternative(Path directory) {
    Path parent = directory.getParent();
    String name = directory.getFileName().toString();

    int maxRetries = 1000;
    for (int i = 2; i < maxRetries + 2; i++) {
      Path alternative = parent.resolve(name + "_" + i);
      if (!Files.exists(alternative)) {
        return alternative;
      }
    }

    throw new DirectoryCreationException("Failed to find alternative directory name.");
  }
}
