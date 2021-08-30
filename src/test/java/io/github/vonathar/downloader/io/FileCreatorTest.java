package io.github.vonathar.downloader.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vonathar.downloader.exception.FileCreationException;
import io.github.vonathar.downloader.testutils.FileUtils;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileCreatorTest {

  private final Path downloadPath =
      Path.of("src/test/resources/file_creator_test");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(downloadPath);
  }

  @AfterEach
  public void cleanup() {
    FileUtils.deleteRecursively(downloadPath);
  }

  @Test
  @SneakyThrows
  public void create_ShouldCreateNewFile() {
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);

    fileCreator.create(inputStream);

    Assertions.assertEquals(1, FileUtils.getFileCount(downloadPath));
  }

  @Test
  @SneakyThrows
  public void create_ShouldCreateNewFileOfCorrectSize() {
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);

    fileCreator.create(inputStream);

    File file = FileUtils.getFiles(downloadPath)[0];
    assertEquals(8, file.length());
  }

  @Test
  @SneakyThrows
  public void create_ShouldNameFilesIncrementally() {
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);

    fileCreator.create(inputStream);
    fileCreator.create(inputStream);
    fileCreator.create(inputStream);

    assertTrue(Files.exists(downloadPath.resolve("0.jpg")));
    assertTrue(Files.exists(downloadPath.resolve("1.jpg")));
    assertTrue(Files.exists(downloadPath.resolve("2.jpg")));
  }

  @Test
  @SneakyThrows
  public void
  create_FileExists_ShouldUseNextAvailableNameThenIncrement() {
    Files.createFile(downloadPath.resolve("0.jpg"));
    Files.createFile(downloadPath.resolve("1.jpg"));
    Files.createFile(downloadPath.resolve("2.jpg"));
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);

    fileCreator.create(inputStream);
    fileCreator.create(inputStream);

    assertTrue(Files.exists(downloadPath.resolve("3.jpg")));
    assertTrue(Files.exists(downloadPath.resolve("4.jpg")));
  }

  @Test
  @SneakyThrows
  public void
  create_MultipleThreads_ShouldCreateAllFilesCorrectly() {
    Thread[] threads = new Thread[20];

    for (int i = 0; i < 20; i++) {
      threads[i] = new Thread(() -> {
        FileCreator fileCreator = new FileCreator(downloadPath);
        InputStream inputStream =
            new ByteArrayInputStream(new byte[1024 * 1024 * 8]);
        try {
          fileCreator.create(inputStream);
        } catch (FileCreationException e) {
          e.printStackTrace();
        }
      });
      threads[i].start();
    }

    for (Thread thread : threads) {
      thread.join();
    }

    for (int i = 0; i < 20; i++) {
      assertTrue(Files.exists(downloadPath.resolve(i + ".jpg")));
    }
  }
}
