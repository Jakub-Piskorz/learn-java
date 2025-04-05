package com.example.controller;

import com.example.dto.FileMetadataDTO;
import com.example.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/**")
    public ResponseEntity<Set<FileMetadataDTO>> filesInDirectory(HttpServletRequest request) throws IOException {
        // Spaces in URL are "%20". We have to deal with that.
        var directoryString = URLDecoder.decode(request.getRequestURI().substring(8), StandardCharsets.UTF_8);
        System.out.println(directoryString);
        var files = fileService.filesInDirectory(directoryString);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    // TODO
    @GetMapping("/search/**")
    public Iterable<FileMetadataDTO> searchFiles(@PathVariable String fileName) {
        // Spaces in URL are "%20". We have to deal with that.
        String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> downloadFile(@PathVariable Long id) throws IOException {
        return new ResponseEntity<>(fileService.downloadFile(id), HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "filePath", required = false) String filePath) throws IOException {
        System.out.println(filePath);
        boolean success = fileService.uploadFile(file, filePath);
        if (success) {
            return new ResponseEntity<>("Successfully uploaded file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFileMetadata(@PathVariable Long id, @RequestBody FileMetadataDTO updatedFile) {
//      TODO
        return new ResponseEntity<>("TODO", HttpStatus.NOT_FOUND);
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
