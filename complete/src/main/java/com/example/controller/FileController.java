package com.example.controller;

import com.example.dto.FileUploadRequest;
import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private final FileMetadataRepository repo;

    @Autowired
    private final FileService fileService;

    public FileController(FileMetadataRepository repo, FileService fileService) {
        this.repo = repo;
        this.fileService = fileService;
    }

    @GetMapping("/")
    public Iterable<FileMetadata> getAllFiles() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Optional<FileMetadata> getFile(@PathVariable Long id) {
        return repo.findById(id);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFile(@RequestBody MultipartFile file) throws IOException {
        FileMetadata uploadedFile = fileService.addFile(file);
        if (uploadedFile != null) {
            return new ResponseEntity<>("Successfully uploaded file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public FileMetadata updateFile(@PathVariable Long id, @RequestBody FileMetadata updatedFile) throws Exception, NullPointerException {
        Optional<FileMetadata> optionalChosenFile = repo.findById(id);
        if (optionalChosenFile.isEmpty()) {
            throw new Exception("No file.");
        }
        FileMetadata chosenFile = optionalChosenFile.get();

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
    public ResponseEntity<String> removeFile(@PathVariable Long id) throws Exception {
        boolean isFileDeleted = fileService.delete(id);
        if (isFileDeleted) {
            return new ResponseEntity<>("Successfully deleted file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("File not found.", HttpStatus.NOT_FOUND);
        }
    }
//
}
