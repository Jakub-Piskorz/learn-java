package com.example;

import com.example.model.Game;
import com.example.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(GameRepository repo) {
        return args -> {
            Game initGame = new Game("spring-sim-2", "Spring Boot Simulator 2", "Spring boot sim returned!!", "anonymous");
            repo.save(initGame);
        };
    }
}
