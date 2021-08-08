package io.github.vonathar;

import static io.github.vonathar.testutils.FileUtils.deleteRecursively;
import static io.github.vonathar.testutils.ReflectionUtils.getField;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DownloaderBuilderTest {

  private final Path rootDirectory = Path.of("src/test/resources/directory_creator");
  private final Path existingDirectory = Path.of("src/test/resources/directory_creator/existing");

  @BeforeEach
  public void setup() throws IOException {
    Files.createDirectories(existingDirectory);
  }

  @AfterEach
  public void cleanup() {
    deleteRecursively(rootDirectory);
  }

  @Test
  public void build_DownloadPathRenamed_ShouldCreateDownloaderWithCorrectDownloadPath() {
    Set<URI> urls = new HashSet<>();
    urls.add(URI.create("https://via.placeholder.com/1"));

    Downloader downloader =
        new DownloaderBuilder()
            .addUrls(urls)
            .downloadPath(existingDirectory)
            .logProgress(false)
            .build();

    Path expected = Path.of("src/test/resources/directory_creator/existing_2");
    Path actual = (Path) getField(downloader, "downloadPath");
    assertEquals(expected, actual);
  }
}
