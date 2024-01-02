package com.jakubpiskorz.learnjava;

import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

public class GameTest {

  @Test
  public void initializeGame() {
    Game tibia = new Game();
    assertEquals("New game.", tibia.start());
  }
}
