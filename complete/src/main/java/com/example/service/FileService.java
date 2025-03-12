package com.example.service;

import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileMetadataRepository repo;

    public FileMetadata uploadFile(MultipartFile file, String filePath) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println("File doesn't exist.");
            return null;
        }

        if (filePath == null) {
            filePath = "";
        }

        Path path = Paths.get("files/" + filePath).normalize();
        Path pathWithFile = Paths.get("files/" + filePath).resolve(Objects.requireNonNull(file.getOriginalFilename()));

//         Check if path exists
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        if (Files.exists(pathWithFile)) {
            System.out.println("File already exists: " + pathWithFile);
            return null;
        }
        Files.copy(file.getInputStream(), pathWithFile);

        FileMetadata fileMetadata = new FileMetadata();
        BasicFileAttributes attrs = Files.readAttributes(pathWithFile, BasicFileAttributes.class);

        fileMetadata.setFileName(file.getOriginalFilename());
        fileMetadata.setFilePath(filePath);
        String createdAt = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        fileMetadata.setCreatedAt(createdAt);
        fileMetadata.setOwnerId(1L);
        return repo.save(fileMetadata);
    }

    public ResponseEntity<InputStreamResource> downloadFile(Long id) throws IOException {
        Optional<FileMetadata> optionalFileMetadata = repo.findById(id);
        assert Objects.requireNonNull(optionalFileMetadata).isPresent();

        FileMetadata fileMetadata = optionalFileMetadata.get();

        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());


        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        InputStream inputStream = Files.newInputStream(filePath);
        InputStreamResource resource = new InputStreamResource(inputStream);

        String contentType = Files.probeContentType(filePath);
        contentType = contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        // Building headers for HTTP response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    public boolean delete(Long id) throws IOException, NullPointerException {
        Optional<FileMetadata> optionalFileMetadata = repo.findById(id);
        if (Objects.requireNonNull(optionalFileMetadata).isEmpty()) {
            return false;
        }
        FileMetadata fileMetadata = optionalFileMetadata.get();

        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());
        Files.delete(filePath);
        repo.delete(fileMetadata);
        return true;
    }

    public FileMetadata findById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Iterable<FileMetadata> searchFiles(String fileName) {
        return repo.findAllByFileNameLike("%" + fileName + "%");
    }
}
