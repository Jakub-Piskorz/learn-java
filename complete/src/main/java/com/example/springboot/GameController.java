package com.example.springboot;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameController {

  @Autowired
  private GameService gameService;

  @GetMapping("/games")
  public List<Game> getAllGames() {
    return gameService.getAllGames();
  }

  @GetMapping("/games/{id}")
  public Game getGame(@PathVariable String id) {
    return gameService.getGame(id);
  }

  @RequestMapping(value = "/games", method = RequestMethod.POST)
  public Game requestMethodName(@RequestBody Game newGame) {
    return gameService.addGame(newGame);
  }

}
