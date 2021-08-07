package io.github.vonathar.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpRequest;
import org.junit.jupiter.api.Test;

class HttpRequestBuilderTest {

  @Test
  void build_ShouldIncludeCorrectRefererHeader() {
    HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
    HttpRequest httpRequest = httpRequestBuilder.build(URI.create("https://via.placeholder/1"));
    String referer = httpRequest.headers().map().get("Referer").get(0);
    String expected = "https://via.placeholder";
    assertEquals(expected, referer);
  }

  @Test
  void build_ShouldIncludeValidUserAgentHeader() {
    HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();
    HttpRequest httpRequest = httpRequestBuilder.build(URI.create("https://via.placeholder/1"));
    String agent = httpRequest.headers().map().get("User-Agent").get(0);
    assertTrue(agent.contains("Mozilla/5.0"));
  }
}
