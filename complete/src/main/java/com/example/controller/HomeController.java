package com.example.controller;

import com.example.config.GlobalVariables;
import com.example.dto.FileMetadataDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Stream;

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
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("empty test endpoint", HttpStatus.OK);
    }
}
