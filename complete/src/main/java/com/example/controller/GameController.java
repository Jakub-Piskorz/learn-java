package com.example.controller;

import java.util.Arrays;
import java.util.List;

import com.example.model.Game;
import com.example.repository.GameRepository;
import com.example.springboot.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/games")
    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }
//
//  @GetMapping("/games/{slug}")
//  public Game getGame(@PathVariable String slug) throws Exception {
//    return gameService.getGame(slug);
//  }
//
//  @RequestMapping(value = "/games", method = RequestMethod.POST)
//  public Game requestMethodName(@RequestBody Game newGame) {
//    return gameService.addGame(newGame);
//  }
//
//  @RequestMapping(value = "/games/{slug}", method = RequestMethod.PUT)
//  public Game requestMethodName(@PathVariable String slug, @RequestBody Game updatedGame) throws Exception {
//    return gameService.updateGame(slug, updatedGame);
//  }
//
//  @RequestMapping(value = "/games/{slug}", method = RequestMethod.DELETE)
//  public void requestMethodName(@PathVariable String slug) throws Exception {
//    gameService.deleteGame(slug);
//  }
//
}
