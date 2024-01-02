package com.jakubpiskorz.learnjava;

import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

public class FileManager {
  public List<Path> paths = new ArrayList<>();

  public Path addPath(String pathString) {
    Path path = Path.of(pathString);
    System.out.println(path);
    paths.add(path);
    return path;
  }
}
