package com.jakubpiskorz.learnjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
    FileManager something = new FileManager();

    System.out.println("Path before");
    System.out.println(something.paths.size());
    something.addPath("C:/Program Files");
    System.out.println("First path:");
    System.out.println(something.paths.size());
    System.out.println("Get path:");
    System.out.println(something.paths.get(0));
    System.out.println("Finished.");

  }

}
