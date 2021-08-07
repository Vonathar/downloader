package io.github.vonathar.io;

import io.github.vonathar.exception.FileCreationException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class FileCreator {

  private final Path downloadPath;
  private final AtomicInteger downloadCount = new AtomicInteger(0);

  public FileCreator(Path downloadPath) {
    this.downloadPath = downloadPath;
  }

  public synchronized void create(InputStream stream) throws FileCreationException {
    String name = String.valueOf(downloadCount.getAndIncrement());
    Path path = downloadPath.resolve(name);
    try {
      Files.copy(stream, path);
    } catch (FileAlreadyExistsException e) {
      create(stream);
    } catch (IOException e) {
      throw new FileCreationException(path, e);
    }
  }
}
