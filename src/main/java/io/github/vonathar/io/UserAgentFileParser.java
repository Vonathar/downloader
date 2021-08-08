package io.github.vonathar.io;

import io.github.vonathar.exception.FileParsingException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserAgentFileParser {

  public List<String> parse() {
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classloader.getResourceAsStream("user_agents");

    if (inputStream == null) {
      throw new FileParsingException("Failed to load user agents from classpath.");
    }

    String fileContent;
    try {
      fileContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new FileParsingException("Failed to read user agents file as a string.", e);
    }

    String[] agents = fileContent.split("\n");
    return List.of(agents);
  }
}
