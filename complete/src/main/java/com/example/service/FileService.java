package com.example.service;

import com.example.dto.FileMetadataDTO;
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
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {

    public static final String FILES_ROOT = "files/";

    // Private helper functions

    private FileMetadataDTO getFileMetadata(Path path) throws IOException {
        var attrs = Files.readAttributes(path, BasicFileAttributes.class);
        return new FileMetadataDTO(
                path.getFileName().toString(),
                Files.size(path),
                attrs.lastModifiedTime().toMillis(),
                Files.isDirectory(path) ? "directory" : "file"  // Type
        );
    }
    private Set<FileMetadataDTO> getFilesMetadata(Stream<Path> pathStream) {
        return pathStream.map(_path -> {
            try {
                return getFileMetadata(_path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toSet());
    }

    // Public services

    public boolean uploadFile(MultipartFile file, String filePath) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println("File doesn't exist.");
            return false;
        }

        if (filePath == null) {
            filePath = "";
        }

        Path path = Paths.get(FILES_ROOT + filePath).normalize();
        Path pathWithFile = Paths.get(FILES_ROOT + filePath).resolve(Objects.requireNonNull(file.getOriginalFilename()));

//         Check if path exists
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        if (Files.exists(pathWithFile)) {
            System.out.println("File already exists: " + pathWithFile);
            return false;
        }
        Files.copy(file.getInputStream(), pathWithFile);
        return true;
    }

    public Set<FileMetadataDTO> filesInDirectory(String directory) throws IOException {
        var path = Paths.get(FILES_ROOT + directory);

        Stream<Path> filePaths = Files.walk(path);
        return getFilesMetadata(filePaths);
    }

    public ResponseEntity<InputStreamResource> downloadFile(String filePathString) throws IOException {
        Path filePath = Paths.get(FILES_ROOT + filePathString);

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
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filePath.getFileName() + "\"");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.EXPIRES, "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    public void delete(String filePath) throws IOException, NullPointerException {
        Path path = Paths.get(FILES_ROOT + filePath).normalize();
        Files.delete(path);
    }

    public Iterable<FileMetadataDTO> searchFiles(String searchString, String directory) throws IOException {
        Stream<Path> walkStream = Files.walk(Paths.get(FILES_ROOT + directory));
        Stream<Path> filteredWalkStream = walkStream.filter(f -> f.getFileName().toString().contains(searchString));
        return getFilesMetadata(filteredWalkStream);
    }

    public Iterable<FileMetadataDTO> searchFiles(String searchString) throws IOException {
        return searchFiles(searchString, "");
    }
}
