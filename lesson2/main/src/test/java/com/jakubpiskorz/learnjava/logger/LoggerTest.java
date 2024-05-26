package com.jakubpiskorz.learnjava.logger;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static com.github.stefanbirkner.systemlambda.SystemLambda.*;

import org.junit.jupiter.api.Test;

public class LoggerTest {

  @Test
  public void testSimpleLogger() throws Exception {
    Logger simpleLogger = new LoggerProcessor(new SimpleLoggerService());

    // Capturing system prints to variables.
    String loudText = tapSystemOut(() -> {
      simpleLogger.loudLog("Loud simple log.");
    });
    String quietText = tapSystemOut(() -> {
      simpleLogger.quietLog("Loud simple log.");
    });

    // Checking if simpleLogger worked correctly.
    assertEquals(loudText, "The message is: Loud simple log.");
    assertEquals(quietText, "Loud simple log.");
  }

  @Test
  public void testNewLineLogger() throws Exception {
    Logger newLineLogger = new LoggerProcessor(new NewLineLoggerService());

    // Capturing system prints to variables.
    String loudText = tapSystemOut(() -> {
      newLineLogger.loudLog("Loud simple log.");
    });
    String quietText = tapSystemOut(() -> {
      newLineLogger.quietLog("Loud simple log.");
    });

    // Checking if simpleLogger worked correctly.
    assertEquals(loudText, "The message is: Loud simple log.\r\n");
    assertEquals(quietText, "Loud simple log.\r\n");
  }
}
