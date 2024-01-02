package com.jakubpiskorz.learnjava;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;

public class FileManagerTest {

  @Test
  public void createFileManagerObject() {
    FileManager testObject = new FileManager();
    assertEquals(0, testObject.paths.size());
    assertNotEquals(1, testObject.paths.size());
  }

  @Test
  public void addPath() {
    FileManager testObject = new FileManager();
    testObject.addPath("/");
    assertEquals(1, testObject.paths.size());
    assertNotEquals(0, testObject.paths.size());

  }
}
