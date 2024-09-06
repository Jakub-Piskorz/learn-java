package com.example.controller;

import com.example.model.Game;
import com.example.repository.GameRepository;
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

    @GetMapping("/games/{slug}")
    public Game getGame(@PathVariable String slug) {
        return gameRepository.findBySlug(slug);
    }

    @PostMapping("/games")
    public Game addGame(@RequestBody Game newGame) {
        return gameRepository.save(newGame);
    }

    @PutMapping("/games/{slug}")
    public Game updateGame(@PathVariable String slug, @RequestBody Game updatedGame) throws Exception {
        Game chosenGame = gameRepository.findBySlug(slug);
        if (updatedGame.getName() != null) {
            chosenGame.setName(updatedGame.getName());
        }
        if (updatedGame.getDescription() != null) {
            chosenGame.setDescription(updatedGame.getDescription());
        }
        if (updatedGame.getPublisher() != null) {
            chosenGame.setPublisher(updatedGame.getPublisher());
        }
        return gameRepository.save(chosenGame);
    }

    @DeleteMapping("/games/{slug}")
    public void removeGame(@PathVariable String slug) throws Exception {
        Game chosenGame = gameRepository.findBySlug(slug);
        gameRepository.delete(chosenGame);
    }
//
}
