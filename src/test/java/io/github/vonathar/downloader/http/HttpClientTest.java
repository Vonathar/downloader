package io.github.vonathar.downloader.http;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.InputStream;
import java.net.URI;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class HttpClientTest {

  @Test
  @SneakyThrows
  public void get_ShouldReturnInputStreamWithValidImage() {
    HttpClient httpClient = new HttpClient();
    URI url = URI.create("https://via.placeholder.com/1");
    InputStream stream = httpClient.getImage(url);
    assertNotNull(ImageIO.read(stream));
  }
}
