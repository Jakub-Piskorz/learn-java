package com.example.controller;

import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private final FileMetadataRepository repo;

    public FileController(FileMetadataRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/")
    public Iterable<FileMetadata> getAllFiles() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public FileMetadata getFile(@PathVariable Long id) {
        return repo.findById(id);
    }

    @PostMapping("/")
    public FileMetadata addFile(@RequestBody FileMetadata newFile) {
        return repo.save(newFile);
    }

    @PutMapping("/{id}")
    public FileMetadata updateFile(@PathVariable Long id, @RequestBody FileMetadata updatedFile) throws Exception {
        FileMetadata chosenFile = repo.findById(id);
        if (chosenFile == null) return null;
        if (updatedFile.getFileName() != null) {
            chosenFile.setFileName(updatedFile.getFileName());
        }
        if (updatedFile.getFilePath() != null) {
            chosenFile.setFilePath(updatedFile.getFilePath());
        }
        if (updatedFile.getFileName() != null) {
            chosenFile.setFileName(updatedFile.getFileName());
        }
        return repo.save(chosenFile);
    }

    @DeleteMapping("/{id}")
    public void removeFile(@PathVariable Long id) throws Exception {
        FileMetadata chosenFile = repo.findById(id);
        if (chosenFile != null) {
            repo.delete(chosenFile);
        }
    }
//
}
