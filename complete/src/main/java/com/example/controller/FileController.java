package com.example.controller;

import com.example.dto.FileDTO;
import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import com.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    public ResponseEntity downloadFile(@PathVariable Long id) throws IOException {
        File fileToDownload = fileService.downloadFile(id);
        if (fileToDownload == null) {
            return new ResponseEntity<>("Something went wrong downloading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileToDownload.getName() + "\"")
                .contentLength(fileToDownload.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(Files.newInputStream(fileToDownload.toPath())));
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "filePath") String filePath) throws IOException {
        FileDTO fileDTO = new FileDTO(filePath, file);
        System.out.println(filePath);
        FileMetadata uploadedFile = fileService.uploadFile(file, filePath);
        if (uploadedFile != null) {
            return new ResponseEntity<>("Successfully uploaded file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public FileMetadata updateFileMetadata(@PathVariable Long id, @RequestBody FileMetadata updatedFile) throws Exception, NullPointerException {
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
