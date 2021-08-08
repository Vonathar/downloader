package io.github.vonathar.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vonathar.exception.FileCreationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileCreatorTest {

  private final Path downloadPath = Path.of("src/test/resources/file_creator_test");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(downloadPath);
  }

  @AfterEach
  public void cleanup() throws IOException {
    Files.walk(downloadPath)
        .sorted(Comparator.reverseOrder())
        .map(Path::toFile)
        .forEach(File::delete);
  }

  @Test
  @SneakyThrows
  public void create_ShouldCreateNewFile() {
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);
    fileCreator.create(inputStream);
    assertTrue(downloadPath.toFile().listFiles().length == 1);
  }

  @Test
  @SneakyThrows
  public void create_ShouldCreateNewFileOfCorrectSize() {
    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);
    fileCreator.create(inputStream);
    File file = downloadPath.toFile().listFiles()[0];
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

    assertTrue(downloadPath.resolve("0").toFile().exists());
    assertTrue(downloadPath.resolve("1").toFile().exists());
    assertTrue(downloadPath.resolve("2").toFile().exists());
  }

  @Test
  @SneakyThrows
  public void create_FileExists_ShouldUseNextAvailableNameThenIncrement() {
    Files.createFile(downloadPath.resolve("0"));
    Files.createFile(downloadPath.resolve("1"));
    Files.createFile(downloadPath.resolve("2"));

    FileCreator fileCreator = new FileCreator(downloadPath);
    InputStream inputStream = new ByteArrayInputStream(new byte[8]);
    fileCreator.create(inputStream);
    fileCreator.create(inputStream);

    assertTrue(downloadPath.resolve("3").toFile().exists());
    assertTrue(downloadPath.resolve("4").toFile().exists());
  }

  @Test
  @SneakyThrows
  public void create_MultipleThreads_ShouldCreateAllFilesCorrectly() {
    Thread[] threads = new Thread[20];

    for (int i = 0; i < 20; i++) {
      threads[i] =
          new Thread(
              () -> {
                FileCreator fileCreator = new FileCreator(downloadPath);
                InputStream inputStream = new ByteArrayInputStream(new byte[1024 * 1024 * 8]);
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
      assertTrue(downloadPath.resolve(String.valueOf(i)).toFile().exists());
    }
  }
}