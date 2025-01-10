package com.example.service;

import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
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

    public FileMetadata uploadFile(MultipartFile file, String filePath) throws IOException {

        if (file == null || file.isEmpty()) {
            System.out.println("File doesn't exist.");
            return null;
        }

        if (filePath == null) {
            filePath = "";
        }

        Path path = Paths.get("files/" + filePath).normalize();
        Path pathWithFile = Paths.get("files/" + filePath).resolve(file.getOriginalFilename());

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
        fileMetadata.setUserId("1");
        return repo.save(fileMetadata);
    }

    public File downloadFile(Long id) {
        Optional<FileMetadata> optionalFileMetadata = repo.findById(id);
        if (optionalFileMetadata.isEmpty()) {
            return null;
        }
        FileMetadata fileMetadata = optionalFileMetadata.get();

        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());
        File fileToDownload = new File(String.valueOf(filePath));
        if (!fileToDownload.exists()) {
            repo.delete(fileMetadata);
            return null;
        }
        System.out.println("File path: " + filePath.toString());

        return fileToDownload;
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

    public Iterable<FileMetadata> searchFiles(String fileName) {
        return repo.findAllByFileNameLike("%" + fileName + "%");
    }
}
