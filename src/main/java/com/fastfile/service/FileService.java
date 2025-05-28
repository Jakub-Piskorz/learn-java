package com.fastfile.service;

import com.fastfile.dto.FileMetadataDTO;
import com.fastfile.dto.SearchFileDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    public static final String FILES_ROOT = "files/";
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

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

    private Path getUserPath(String directory) {
        if (directory == null) directory = "";

        // 🔒 Safety check against unsafe paths.
        Path path = Paths.get(directory);
        if (directory.contains("\u0000") || path.isAbsolute()) {
            throw new IllegalArgumentException("Unsafe path");
        }
        Path normalized = path.normalize();
        for (Path part : normalized) {
            if (part.toString().equals("..")) {
                throw new IllegalArgumentException("Unsafe path");
            }
        }

        String userId = authService.getUserId();
        return Paths.get(FILES_ROOT + userId + "/" + directory);
    }

    private Path getUserPath() {
        return getUserPath(null);
    }

    private long bytesInside(Path path) throws IOException {
        if (path.toFile().isFile()) {
            return path.toFile().length();
        }
        try (Stream<Path> stream = Files.walk(path)) {
            return stream.filter(p -> p.toFile().isFile())
                    .mapToLong(p -> p.toFile().length())
                    .sum();
        }
    }

    private boolean isStorageLimitExceeded(long newFileSize) throws IOException {
//        TODO: We probably want to store current user's store usage in DB instead of calculating it on demand. It will make uploads quicker.
        long currentUsage = bytesInside(getUserPath());
        return (newFileSize + currentUsage) > userService.getUserStorageLimit();
    }

    private boolean isStorageLimitExceeded() throws IOException {
        return isStorageLimitExceeded(0);
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

        Path path = getUserPath(filePath).normalize();
        Path pathWithFile = getUserPath(filePath).resolve(Objects.requireNonNull(file.getOriginalFilename()));

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

    public Set<FileMetadataDTO> filesInDirectory(String directory, int maxDepth) throws IOException {
        Path path = getUserPath(directory);

        Stream<Path> walkStream = Files.walk(path, maxDepth).skip(1);
        Set<FileMetadataDTO> filesMetadata = getFilesMetadata(walkStream);
        walkStream.close();
        return filesMetadata;
    }

    public Set<FileMetadataDTO> filesInDirectory(String directory) throws IOException {
        return filesInDirectory(directory, 1);
    }

    public ResponseEntity<InputStreamResource> downloadFile(String directory) throws IOException {
        Path filePath = getUserPath(directory);

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
        Path path = getUserPath(filePath).normalize();
        Files.delete(path);
    }

    public Iterable<FileMetadataDTO> searchFiles(SearchFileDTO searchFile) throws IOException {
        if (searchFile.getFileName() == null || searchFile.getFileName().isEmpty()) {
            throw new IllegalArgumentException("File name is empty");
        }
        if (searchFile.getDirectory() == null) searchFile.setDirectory("");
        Stream<Path> walkStream = Files.walk(getUserPath(searchFile.getDirectory()));
        // Skip(1), because it starts the list with itself (directory)
        Stream<Path> filteredWalkStream = walkStream.skip(1).filter(f -> f.getFileName().toString().contains(searchFile.getFileName()));
        var filesMetadata = getFilesMetadata(filteredWalkStream);
        walkStream.close();
        return filesMetadata;
    }

    public boolean createDirectory(String path) throws IOException {
        if (path != null && !Files.exists(getUserPath(path))) {
            Files.createDirectories(getUserPath(path));
            return true;
        } else {
            return false;
        }
    }


    @SneakyThrows
    public boolean deleteRecursively(String directory) {
        Path baseDir = getUserPath().toAbsolutePath();
        Path path = getUserPath(directory);
        if (path.toAbsolutePath().equals(baseDir)) {
            return false;
        }
        try (Stream<Path> walkStream = Files.walk(path)) {
            walkStream.sorted(Comparator.reverseOrder()).forEach(p -> {
                try {
                    Files.delete(p);
                } catch (IOException e) {
                    // Log or handle the exception if needed
                    throw new UncheckedIOException(e);
                }
            });
        }
        return true;
    }
}
