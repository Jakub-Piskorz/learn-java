package com.example.controller;

import com.example.model.Game;
import com.example.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    @Autowired
    private final GameRepository gameRepository;

    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @GetMapping("/")
    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/{slug}")
    public Game getGame(@PathVariable String slug) {
        return gameRepository.findBySlug(slug);
    }

    @PostMapping("/")
    public Game addGame(@RequestBody Game newGame) {
        return gameRepository.save(newGame);
    }

    @PutMapping("/{slug}")
    public Game updateGame(@PathVariable String slug, @RequestBody Game updatedGame) throws Exception {
        Game chosenGame = gameRepository.findBySlug(slug);
        if (chosenGame == null) return null;
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

    @DeleteMapping("/{slug}")
    public void removeGame(@PathVariable String slug) throws Exception {
        Game chosenGame = gameRepository.findBySlug(slug);
        gameRepository.delete(chosenGame);
    }
//
}
