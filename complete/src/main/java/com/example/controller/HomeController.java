package com.example.controller;

import com.example.config.GlobalVariables;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@Profile("dev")
public class HomeController {
    private final GlobalVariables env;


    public HomeController(GlobalVariables env) {
        this.env = env;
    }

    @GetMapping("/")
    public String home() {
        return "Hello World";
    }

    @GetMapping("/test")
    public void secured() throws IOException {
        Path path = Paths.get("files/").normalize();
        Files.walk(path, 1)
                .forEach(System.out::println);
    }
}
