package com.example.service;

import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileMetadataRepository repo;

    public FileMetadata addFile(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return null;
        }

        Path destinationPath = Paths.get("files/").resolve(file.getOriginalFilename());
        if (Files.exists(destinationPath)) {
            return null;
        }
        Files.copy(file.getInputStream(), destinationPath);

        FileMetadata fileMetadata = new FileMetadata();
        BasicFileAttributes attrs = Files.readAttributes(destinationPath, BasicFileAttributes.class);

        fileMetadata.setFileName(file.getOriginalFilename());
        fileMetadata.setFilePath("");
        String createdAt = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        fileMetadata.setCreatedAt(createdAt);
        fileMetadata.setUserId("1");
        return repo.save(fileMetadata);
    }

    public boolean delete(Long id) throws IOException {
        Optional<FileMetadata> optionalFileMetadata = repo.findById(id);
        if (optionalFileMetadata.isEmpty()) {
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
}
