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
@RequestMapping(FileService.FILES_ENDPOINT)
public class FileController {

    private String path;

    @Autowired
    private final FileService fileService;

    private String decodeURL(HttpServletRequest request) {
        return URLDecoder.decode(request.getRequestURI().substring((FileService.FILES_ENDPOINT + path).length()), StandardCharsets.UTF_8);
    }

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path = "/**")
    public ResponseEntity<Set<FileMetadataDTO>> filesInDirectory(HttpServletRequest request) throws IOException {
        var path = decodeURL(request);
        var files = fileService.filesInDirectory(path);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping(path = "/search/**")
    public Iterable<FileMetadataDTO> searchFiles(HttpServletRequest request) throws IOException {
        var path = decodeURL(request);
        return fileService.searchFiles(path);
    }

    @GetMapping(path = "/download/**")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request) throws IOException {
        var path = decodeURL(request);
        return fileService.downloadFile(path);
    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestPart("filePath") String filePath) throws IOException {
        boolean success = fileService.uploadFile(file, filePath);
        if (success) {
            return new ResponseEntity<>("Successfully uploaded file.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/**")
    public ResponseEntity<String> removeFile(HttpServletRequest request) throws Exception {
        var filePath = decodeURL(request);
        fileService.delete(filePath);
        return new ResponseEntity<>("Successfully deleted file.", HttpStatus.OK);
    }
//
}
