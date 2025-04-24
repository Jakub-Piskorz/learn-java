package com.fastfile.service;

import com.fastfile.dto.FileMetadataDTO;
import com.fastfile.dto.SearchFileDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    protected final Log logger = LogFactory.getLog(getClass());
    // Private helper functions

    private FileMetadataDTO getFileMetadata(Path path) throws IOException {
        var attrs = Files.readAttributes(path, BasicFileAttributes.class);
        return new FileMetadataDTO(
                path.getFileName().toString(),
                Files.size(path),
                attrs.lastModifiedTime().toMillis(),
                Files.isDirectory(path) ? "directory" : "file",  // Type
                path.toString()
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

    private String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1) : "";
    }

    private String getContentTypeFromExtension(String extension) {
        return switch (extension.toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "jpg" -> "image/jpeg";
            case "png" -> "image/png";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
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

        // Check if path exists
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

        Stream<Path> walkStream = Files.walk(path).skip(1);
        return getFilesMetadata(walkStream);
    }

    public ResponseEntity<InputStreamResource> downloadFile(String filePathString) throws IOException {
        Path filePath = Paths.get(FILES_ROOT + filePathString);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        InputStream inputStream = Files.newInputStream(filePath);
        InputStreamResource resource = new InputStreamResource(inputStream);

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            String fileExtension = getFileExtension(filePath.getFileName().toString());
            contentType = getContentTypeFromExtension(fileExtension);
        }

        // Building headers for HTTP response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\"" + URLEncoder.encode(filePath.getFileName().toString(), StandardCharsets.UTF_8) + "\"");
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

    public Iterable<FileMetadataDTO> searchFiles(SearchFileDTO searchFile) throws IOException {
        if (searchFile.getFileName() == null || searchFile.getFileName().isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }
        if (searchFile.getDirectory() == null) searchFile.setDirectory("");
        Stream<Path> walkStream = Files.walk(Paths.get(FILES_ROOT + searchFile.getDirectory()));
        // Skip(1), because it starts the list with itself (directory)
        Stream<Path> filteredWalkStream = walkStream.skip(1).filter(f -> f.getFileName().toString().contains(searchFile.getFileName()));
        return getFilesMetadata(filteredWalkStream);
    }

    public boolean createDirectory(String path) throws IOException {
        if (path != null && !Files.exists(Paths.get(FILES_ROOT + path))) {
            Files.createDirectories(Paths.get(FILES_ROOT + path));
            return true;
        } else {
            return false;
        }
    }
}
