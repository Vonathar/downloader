package io.github.vonathar.downloader.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserAgentGeneratorTest {

  @Test
  public void random_ReturnsValidString() {
    UserAgentGenerator userAgentGenerator = new UserAgentGenerator();
    String randomAgent = userAgentGenerator.random();
    assertFalse(randomAgent.isBlank());
  }

  @Test
  public void random_ReturnValidUserAgent() {
    UserAgentGenerator userAgentGenerator = new UserAgentGenerator();
    String randomAgent = userAgentGenerator.random();
    System.out.println(randomAgent);
    assertTrue(randomAgent.contains("Mozilla/5.0"));
  }

  @Test
  public void random_Every500SequentialAgentsMustBeUnique() {
    UserAgentGenerator userAgentGenerator = new UserAgentGenerator();
    Set<String> agents = new HashSet<>();
    for (int i = 0; i < 500; i++) {
      agents.add(userAgentGenerator.random());
    }
    assertEquals(500, agents.size());
  }

  @Test
  public void random_ShouldUltimatelyUseAllAgents() {
    UserAgentGenerator userAgentGenerator = new UserAgentGenerator();
    Set<String> agents = new HashSet<>();
    for (int i = 0; i < 10000; i++) {
      agents.add(userAgentGenerator.random());
    }
    assertEquals(1000, agents.size());
  }
}
