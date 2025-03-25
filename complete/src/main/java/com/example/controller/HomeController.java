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

    @GetMapping("/test/**")
    public ResponseEntity<Object> secured(HttpServletRequest request) throws IOException {

        var filePathString = "files" + request.getRequestURI().substring(5);
        var filePath = Paths.get(filePathString);

        Stream<Path> filePaths = Files.walk(filePath);
        Set<FileMetadataDTO> metadata = new java.util.HashSet<>(Set.of());
        filePaths.forEach(path -> {
            try {
                var attrs = Files.readAttributes(path, BasicFileAttributes.class);
                metadata.add(new FileMetadataDTO(
                        path.getFileName().toString(),
                        Files.size(path),
                        attrs.lastModifiedTime().toMillis(),
                        Files.isDirectory(path) ? "directory" : "file"  // Type
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return ResponseEntity.ok(metadata);
    }
}
