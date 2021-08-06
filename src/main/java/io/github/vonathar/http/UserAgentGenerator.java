package io.github.vonathar.http;

import io.github.vonathar.exception.FileParsingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class UserAgentGenerator {

  private final List<String> values = new ArrayList<>();
  private final Set<Integer> usedIndexes = new HashSet<>();

  public UserAgentGenerator() {
    loadList();
  }

  private void loadList() {
    Path file = Path.of("src/main/resources/user_agents");
    try (Stream<String> stream = Files.lines(file)) {
      stream.forEach(values::add);
    } catch (IOException e) {
      throw new FileParsingException(file, e);
    }
  }

  public String random() {
    int randomIndex = ThreadLocalRandom.current().nextInt(0, values.size());
    if (usedIndexes.contains(randomIndex)) {
      return random();
    }
    usedIndexes.add(randomIndex);
    if (usedIndexes.size() >= values.size() / 2) {
      usedIndexes.clear();
    }
    return values.get(randomIndex);
  }
}
