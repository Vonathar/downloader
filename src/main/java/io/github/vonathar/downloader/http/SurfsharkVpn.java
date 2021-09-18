package io.github.vonathar.downloader.http;

import io.github.vonathar.downloader.exception.VpnRestartException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SurfsharkVpn {

  private final String password;

  public SurfsharkVpn(String password) {
    this.password = password;
  }

  public void restart() {
    log.info("Restarting VPN..");

    String[] cmd = {
        "/bin/bash", "-c",
        String.format(
            "echo %s| sudo -S %s", password,
            "sudo surfshark-vpn status"
                + " && sudo surfshark-vpn down"
                + " && echo '0' | sudo surfshark-vpn attack"
                + " && exit")};

    try {
      Process process = Runtime.getRuntime().exec(cmd);
      execute(process);
    } catch (IOException e) {
      throw new VpnRestartException("Failed to restart VPN.", e);
    }
  }

  private void execute(Process process) throws IOException {
    String line;
    BufferedReader input = new BufferedReader(
        new InputStreamReader(process.getInputStream()));
    while ((line = input.readLine()) != null) {
      log.debug("Shell output: {}", line);
      if (line.contains("Not connected to Surfshark VPN")) {
        log.warn("Surfshark VPN is not connected.");
        return;
      }
      if (line.startsWith("Your new IP")) {
        String ip = line.substring(line.lastIndexOf(':') + 2);
        log.info("Successfully restarted VPN. New IP: {}", ip);
      }
    }
    input.close();
  }
}
