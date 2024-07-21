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

  @GetMapping("/games/{slug}")
  public Game getGame(@PathVariable String slug) throws Exception {
    return gameService.getGame(slug);
  }

  @RequestMapping(value = "/games", method = RequestMethod.POST)
  public Game requestMethodName(@RequestBody Game newGame) {
    return gameService.addGame(newGame);
  }

  @RequestMapping(value = "/games/{slug}", method = RequestMethod.PUT)
  public Game requestMethodName(@PathVariable String slug, @RequestBody Game updatedGame) throws Exception {
    return gameService.updateGame(slug, updatedGame);
  }

  @RequestMapping(value = "/games/{slug}", method = RequestMethod.DELETE)
  public void requestMethodName(@PathVariable String slug) throws Exception {
    gameService.deleteGame(slug);
  }

}
