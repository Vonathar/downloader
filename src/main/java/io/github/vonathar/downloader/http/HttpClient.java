package io.github.vonathar.downloader.http;

import io.github.vonathar.downloader.exception.HttpRequestException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient {

  private final java.net.http.HttpClient client =
      java.net.http.HttpClient.newBuilder().build();
  HttpRequestBuilder httpRequestBuilder = new HttpRequestBuilder();

  public InputStream getImage(URI url) throws HttpRequestException {

    HttpRequest httpRequest = httpRequestBuilder.build(url);
    return sendRequest(httpRequest);
  }

  private InputStream sendRequest(HttpRequest httpRequest)
      throws HttpRequestException {
    try {
      return client
          .send(httpRequest,
              responseInfo
                  -> HttpResponse.BodySubscribers.ofInputStream())
          .body();
    } catch (IOException | InterruptedException e) {
      throw new HttpRequestException("Failed to send HTTP request.",
          e);
    }
  }
}
