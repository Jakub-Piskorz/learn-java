package com.example.service;

import com.example.dto.FileMetadataDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class FileService {

    public boolean uploadFile(MultipartFile file, String filePath) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println("File doesn't exist.");
            return false;
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
            return false;
        }
        Files.copy(file.getInputStream(), pathWithFile);
        return true;
    }

    public Set<FileMetadataDTO> filesInDirectory(String directory) throws IOException {
        var path = Paths.get(directory);

        Stream<Path> filePaths = Files.walk(path);
        Set<FileMetadataDTO> metadata = new java.util.HashSet<>(Set.of());
        filePaths.forEach(_path -> {
            try {
                var attrs = Files.readAttributes(_path, BasicFileAttributes.class);
                metadata.add(new FileMetadataDTO(
                        _path.getFileName().toString(),
                        Files.size(_path),
                        attrs.lastModifiedTime().toMillis(),
                        Files.isDirectory(_path) ? "directory" : "file"  // Type
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return metadata;
    }

    public String downloadFile(Long id) throws IOException {
//        Optional<FileMetadata> optionalFileMetadata = repo.findById(id);
//        assert Objects.requireNonNull(optionalFileMetadata).isPresent();
//
//        FileMetadata fileMetadata = optionalFileMetadata.get();
//
//        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());
//
//
//        if (!Files.exists(filePath)) {
//            return ResponseEntity.notFound().build();
//        }
//
//        InputStream inputStream = Files.newInputStream(filePath);
//        InputStreamResource resource = new InputStreamResource(inputStream);
//
//        String contentType = Files.probeContentType(filePath);
//        contentType = contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
//
//        // Building headers for HTTP response
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"");
//        headers.add(HttpHeaders.PRAGMA, "no-cache");
//        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
//        headers.add(HttpHeaders.EXPIRES, "0");

//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.parseMediaType(contentType))
//                .body(resource);
//      "TODO"
        return "TO DO";
    }

    public boolean delete(Long id) throws IOException, NullPointerException {
//
//        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());
//        Files.delete(filePath);
        // TODO
        return true;
    }
//    TODO
//    public FileMetadata findById(Long id) {
//        return repo.findById(id).orElseThrow();
//    }
//
    public Iterable<FileMetadataDTO> searchFiles(String fileName) {
        DirectoryStream.Filter<Path> filter = new DirectoryStream.Filter<Path>() {
            public boolean accept(Path file) throws IOException {
                return (Files.size(file) > 8192L);
            }
        };
    }
}
