package io.github.vonathar.io;

import static io.github.vonathar.testutils.FileUtils.deleteRecursively;
import static io.github.vonathar.testutils.FileUtils.getFileCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DirectoryCreatorTest {

  private final Path rootDirectory = Path.of("src/test/resources/directory_creator");
  private final Path existingDirectory = Path.of("src/test/resources/directory_creator/existing");
  private final Path existingFile = existingDirectory.resolve("sample_file");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(existingDirectory);
    Files.createFile(existingFile);
  }

  @AfterEach
  public void cleanup() {
    deleteRecursively(rootDirectory);
  }

  @Test
  void create_DeleteExisting_ExistingDirectory_ShouldDeleteDirectoryThenCreateIt() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.DELETE_EXISTING);
    assertEquals(1, getFileCount(existingDirectory));

    directoryCreator.create(existingDirectory);

    assertEquals(0, getFileCount(existingDirectory));
  }

  @Test
  void create_DeleteExisting_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.DELETE_EXISTING);
    deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    assertEquals(1, getFileCount(rootDirectory));
  }

  @Test
  void create_RenameCreated_ExistingDirectory_ShouldFindAlternativeNameThenCreateDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.RENAME_CREATED);

    directoryCreator.create(existingDirectory);
    directoryCreator.create(existingDirectory);
    directoryCreator.create(existingDirectory);

    assertTrue(Files.exists(rootDirectory.resolve("existing_2")));
    assertTrue(Files.exists(rootDirectory.resolve("existing_3")));
    assertTrue(Files.exists(rootDirectory.resolve("existing_4")));
  }

  @Test
  void create_RenameCreated_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.RENAME_CREATED);
    deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    assertEquals(1, getFileCount(rootDirectory));
  }

  @Test
  void create_ReuseExisting_ExistingDirectory_ShouldReuseExistingDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);

    directoryCreator.create(existingDirectory);

    assertEquals(1, getFileCount(rootDirectory));
  }

  @Test
  void create_ReuseExisting_ExistingDirectory_ShouldNotDeleteExistingFiles() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);

    directoryCreator.create(existingDirectory);

    assertEquals(1, getFileCount(existingDirectory));
  }

  @Test
  void create_ReuseExisting_NoExistingDirectory_ShouldCreateDirectory() {
    DirectoryCreator directoryCreator = new DirectoryCreator(DirectoryStrategy.REUSE_EXISTING);
    deleteRecursively(existingDirectory);

    directoryCreator.create(existingDirectory);

    assertEquals(1, getFileCount(rootDirectory));
  }
}
