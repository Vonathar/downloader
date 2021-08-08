package io.github.vonathar.testutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;

public class FileUtils {

  public static int getFileCount(Path path) {
    File directory = path.toFile();
    return Objects.requireNonNull(directory.list()).length;
  }

  public static void deleteRecursively(Path directory) {
    try {
      Files.walk(directory)
          .sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static File[] getFiles(Path path) {
    return path.toFile().listFiles();
  }
}
