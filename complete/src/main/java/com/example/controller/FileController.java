package com.example.controller;

import com.example.dto.FileMetadataDTO;
import com.example.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
        var files = fileService.filesInDirectory(directoryString);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/search/**")
    public Iterable<FileMetadataDTO> searchFiles(HttpServletRequest request) throws IOException {
        // Spaces in URL are "%20". We have to deal with that.
        var filePath = URLDecoder.decode(request.getRequestURI().substring(8), StandardCharsets.UTF_8);
        String decodedFileName = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        return fileService.searchFiles(decodedFileName);
    }

    @GetMapping("/{filePath}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filePath) throws IOException {
        return fileService.downloadFile(filePath);
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

    @DeleteMapping("/{filePath}")
    public ResponseEntity<String> removeFile(@PathVariable String filePath) throws Exception {
        fileService.delete(filePath);
        return new ResponseEntity<>("Successfully deleted file.", HttpStatus.OK);
    }
//
}
