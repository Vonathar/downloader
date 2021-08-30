package io.github.vonathar.downloader.http;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class SurfsharkVpnTest {

  @Test
  @Disabled
    // To be run manually.
  void restart_ShouldRestartTheVpn() {
    String password = "";
    SurfsharkVpn surfsharkVpn = new SurfsharkVpn(password);
    surfsharkVpn.restart();
  }
}