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
    private String decodeURL(HttpServletRequest request, int substring) {
        return URLDecoder.decode(request.getRequestURI().substring(substring), StandardCharsets.UTF_8);
    }

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/**")
    public ResponseEntity<Set<FileMetadataDTO>> filesInDirectory(HttpServletRequest request) throws IOException {
        var path = decodeURL(request, 14);
        var files = fileService.filesInDirectory(path);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/search/**")
    public Iterable<FileMetadataDTO> searchFiles(HttpServletRequest request) throws IOException {
        var path = decodeURL(request, 21);
        return fileService.searchFiles(path);
    }

    @GetMapping("/download/**")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request) throws IOException {
        var path = decodeURL(request, 23);
        return fileService.downloadFile(path);
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "filePath", required = false) String filePath) throws IOException {
        boolean success = fileService.uploadFile(file, filePath);
        if (success) {
            return new ResponseEntity<>("Successfully uploaded file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/**")
    public ResponseEntity<String> removeFile(HttpServletRequest request) throws Exception {
        var filePath = decodeURL(request, 14);
        fileService.delete(filePath);
        return new ResponseEntity<>("Successfully deleted file.", HttpStatus.OK);
    }
//
}
