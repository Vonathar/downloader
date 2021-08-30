package io.github.vonathar.downloader.http;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestBuilder {

  private final UserAgentGenerator userAgentGenerator =
      new UserAgentGenerator();

  public HttpRequest build(URI url) {

    String userAgent = userAgentGenerator.random();
    String referer = getReferer(url);

    return HttpRequest.newBuilder()
        .uri(url)
        .header("Accept-Encoding", "gzip, deflate, br")
        .header("Accept-Language", "en-GB,en;q=0.5")
        .header("User-Agent", userAgent)
        .header("Accept", "image/webp,*/*;q=0.8")
        .header("Referer", referer)
        .header("DNT", "1")
        .header("Upgrade-Insecure-Requests", "1")
        .header("Sec-Fetch-Dest", "document")
        .header("Sec-Fetch-Mode", "navigate")
        .header("Sec-Fetch-Site", "same-site")
        .header("Sec-Fetch-User", "?1")
        .header("Pragma", "no-cache")
        .header("Cache-Control", "no-cache")
        .GET()
        .build();
  }

  private String getReferer(URI url) {
    return url.getScheme() + "://" + url.getRawAuthority();
  }
}
