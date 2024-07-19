package com.example.springboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GameService {

  private List<Game> games = new ArrayList<Game>(Arrays.asList(
      new Game("tibia", "Tibia", "Oldest running MMORPG"),
      new Game("wow", "World of Warcraft", "Asmon dungeon"),
      new Game("fortnite", "Fortnite", "Cringe because of kids, but game is actually fun.")));

  public List<Game> getAllGames() {
    return games;
  }

  public Game getGame(String id) {
    Game game = games.stream().filter(g -> g.getId().equals(id)).findFirst().get();
    return game;
  }

  public Game addGame(Game newGame) {
    games.add(newGame);
    return newGame;
  }
}
