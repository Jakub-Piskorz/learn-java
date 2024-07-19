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

  public Game updateGame(String id, Game updatedGame) throws Exception {
    for (int i = 0; i < games.size(); i++) {
      Game game = games.get(i);
      if (game.getId().equals(id)) {
        games.set(i, updatedGame);
        return games.get(i);
      }
    }
    throw new Exception("Game with id: " + id + "not found");
  }

  public void deleteGame(String id) throws Exception {
    for (int i = 0; i < games.size(); i++) {
      Game game = games.get(i);
      if (game.getId().equals(id)) {
        games.remove(i);
        return;
      }
    }
    throw new Exception("Game with id: " + id + "not found");
  }
}
