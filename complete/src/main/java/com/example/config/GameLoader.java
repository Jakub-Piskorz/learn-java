package com.example.config;

import com.example.model.Game;
import com.example.repository.GameRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Component
public class GameLoader implements CommandLineRunner {
    private final GameRepository repo;
    private final ObjectMapper objectMapper;

    public GameLoader(GameRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        try(InputStream inputStream = TypeReference.class.getResourceAsStream("/jsons/games.json")) {
            if (repo.count() < 2) {
                repo.saveAll(objectMapper.readValue(inputStream, new TypeReference<List<Game>>(){}));
            }
        }
    }
}