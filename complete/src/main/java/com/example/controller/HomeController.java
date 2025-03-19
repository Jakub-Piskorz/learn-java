package com.example.controller;

import com.example.config.GlobalVariables;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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

    @GetMapping("/test/**")
    public ResponseEntity<Object> secured(HttpServletRequest request) throws IOException {

        var filePathString = "files" + request.getRequestURI().substring(5);
        var filePath = Paths.get(filePathString);

        var exists = Files.exists(filePath);
        if (!exists) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        var attrs = Files.readAttributes(filePath, BasicFileAttributes.class);

        var fileMetadata = new FileMetadata(
                filePath.getFileName().toString(),
                Files.size(filePath),
                attrs.lastModifiedTime().toMillis(),
                Files.isDirectory(filePath) ? "directory" : "file"  // Type
        );
        return ResponseEntity.ok(fileMetadata);
    }
    // Simple DTO to hold metadata
    public record FileMetadata(String name, long size, long lastModified, String type) {}
}
