package io.github.vonathar.io;

import static io.github.vonathar.io.UrlFileParserTest.TestUtils.getExpectedUrls;
import static io.github.vonathar.io.UrlFileParserTest.TestUtils.writeTestUrls;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class UrlFileParserTest {

  private final Path urlFile = Path.of("src/test/resources/urls");
  private final UrlFileParser urlFileParser = new UrlFileParser();

  @AfterEach
  public void cleanup() throws IOException {
    Files.deleteIfExists(urlFile);
  }

  @Test
  @SneakyThrows
  public void parse_HasDuplicates_ShouldParseAllUrlsFromGivenFile() {
    writeTestUrls(10, urlFile, true, false);
    Set<URI> expectedUrls = getExpectedUrls(10, true, false);
    Set<URI> urls = urlFileParser.parse(urlFile);
    assertTrue(urls.containsAll(expectedUrls) && expectedUrls.containsAll(urls));
  }

  @Test
  @SneakyThrows
  public void parse_UniqueUrls_ShouldParseAllUrlsFromGivenFile() {
    writeTestUrls(10, urlFile, false, false);
    Set<URI> expectedUrls = getExpectedUrls(10, false, false);
    Set<URI> urls = urlFileParser.parse(urlFile);
    assertTrue(urls.containsAll(expectedUrls) && expectedUrls.containsAll(urls));
  }

  @Test
  @SneakyThrows
  public void parse_BadUrls_ShouldParseOnlyValidUrlsFromGivenFile() {
    writeTestUrls(10, urlFile, false, true);
    Set<URI> expectedUrls = getExpectedUrls(10, false, true);
    Set<URI> urls = urlFileParser.parse(urlFile);
    assertTrue(urls.containsAll(expectedUrls) && expectedUrls.containsAll(urls));
  }

  static class TestUtils {

    @SneakyThrows
    static void writeTestUrls(int n, Path urlFile, boolean hasDuplicates, boolean hasBadUrls) {
      StringBuilder output = new StringBuilder();
      for (int i = 0; i < n; i++) {
        if (hasBadUrls && i % 3 == 0) {
          output.append("bad_url").append(i).append("\n");
        } else {
          output.append("https://www.goodurl.com/").append(i).append("\n");
        }

        if (hasDuplicates && (i + 1) < n) {
          output.append("https://www.goodurl.com/").append(i).append("\n");
          i++;
        }
      }
      Files.writeString(urlFile, output.toString());
    }

    static Set<URI> getExpectedUrls(int n, boolean hasDuplicates, boolean hasBadUrls) {
      Set<URI> expectedUrls = new HashSet<>();
      for (int i = 0; i < n; i++) {
        if (!hasBadUrls || i % 3 != 0) {
          expectedUrls.add(URI.create("https://www.goodurl.com/" + i));
        }
        if (hasDuplicates) {
          i++;
        }
      }
      return expectedUrls;
    }
  }
}
