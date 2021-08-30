package io.github.vonathar.downloader.http;

import io.github.vonathar.downloader.io.UserAgentFileParser;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class UserAgentGenerator {

  private final List<String> values = new ArrayList<>();
  private final Set<Integer> usedIndexes = new HashSet<>();

  public UserAgentGenerator() {
    UserAgentFileParser userAgentFileParser =
        new UserAgentFileParser();
    values.addAll(userAgentFileParser.parse());
  }

  public String random() {
    int randomIndex =
        ThreadLocalRandom.current().nextInt(0, values.size());
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
