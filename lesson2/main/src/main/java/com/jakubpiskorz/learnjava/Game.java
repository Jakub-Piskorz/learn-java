package com.jakubpiskorz.learnjava;

public class Game {
  private String stateOfGame = "quit";

  public String start() {
    if (stateOfGame == "quit")
      stateOfGame = "started";
    return "New game.";
  }
}