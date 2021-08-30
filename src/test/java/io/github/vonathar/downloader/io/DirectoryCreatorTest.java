package io.github.vonathar.downloader.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.vonathar.downloader.testutils.FileUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectoryCreatorTest {

  private final Path rootDirectory =
      Path.of("src/test/resources/directory_creator");
  private final Path existingDirectory =
      Path.of("src/test/resources/directory_creator/existing");
  private final Path existingFile =
      existingDirectory.resolve("sample_file");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(existingDirectory);
    Files.createFile(existingFile);
    System.setProperty("user.home", rootDirectory.toString());
  }

  @AfterEach
  public void cleanup() {
    FileUtils.deleteRecursively(rootDirectory);
  }

  @Test
  void
  create_DeleteExisting_ExistingDirectory_ShouldDeleteDirectoryThenCreateIt() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.DELETE_EXISTING);
    Assertions.assertEquals(
        1, FileUtils.getFileCount(existingDirectory));

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(
        0, FileUtils.getFileCount(existingDirectory));
  }

  @Test
  void
  create_DeleteExisting_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.DELETE_EXISTING);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(1,
        FileUtils.getFileCount(rootDirectory));
  }

  @Test
  void
  create_DeleteExisting_NoDirectoryGiven_ShouldCreateNewDirectoryInUserDownloads() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.DELETE_EXISTING);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(null);

    Path downloadDirectory = rootDirectory.resolve("Downloads");
    Assertions.assertEquals(
        1, FileUtils.getFileCount(downloadDirectory));
  }

  @Test
  void
  create_RenameCreated_ExistingDirectory_ShouldFindAlternativeNameThenCreateDirectory() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.RENAME_CREATED);

    directoryCreator.create(existingDirectory);
    directoryCreator.create(existingDirectory);
    directoryCreator.create(existingDirectory);

    assertTrue(Files.exists(rootDirectory.resolve("existing_2")));
    assertTrue(Files.exists(rootDirectory.resolve("existing_3")));
    assertTrue(Files.exists(rootDirectory.resolve("existing_4")));
  }

  @Test
  void
  create_RenameCreated_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.RENAME_CREATED);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(1,
        FileUtils.getFileCount(rootDirectory));
  }

  @Test
  void
  create_RenameCreated_NoDirectoryGiven_ShouldCreateNewDirectoryInUserDownloads() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.RENAME_CREATED);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(null);

    Path downloadDirectory = rootDirectory.resolve("Downloads");
    Assertions.assertEquals(
        1, FileUtils.getFileCount(downloadDirectory));
  }

  @Test
  void
  create_ReuseExisting_ExistingDirectory_ShouldReuseExistingDirectory() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(1,
        FileUtils.getFileCount(rootDirectory));
  }

  @Test
  void
  create_ReuseExisting_ExistingDirectory_ShouldNotDeleteExistingFiles() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(
        1, FileUtils.getFileCount(existingDirectory));
  }

  @Test
  void
  create_ReuseExisting_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    Assertions.assertEquals(1,
        FileUtils.getFileCount(rootDirectory));
  }

  @Test
  void
  create_ReuseExisting_NoDirectoryGiven_ShouldCreateNewDirectoryInUserDownloads() {
    DirectoryCreator directoryCreator =
        new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);
    FileUtils.deleteRecursively(existingDirectory);

    directoryCreator.create(null);

    Path downloadDirectory = rootDirectory.resolve("Downloads");
    Assertions.assertEquals(
        1, FileUtils.getFileCount(downloadDirectory));
  }
}
