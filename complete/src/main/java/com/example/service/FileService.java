package com.example.service;

import com.example.dto.FileUploadRequest;
import com.example.model.FileMetadata;
import com.example.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileService {
    @Autowired
    private FileMetadataRepository repo;

    public FileMetadata addFile(FileUploadRequest fileUpload) throws IOException {
        FileMetadata fileMetadata = fileUpload.getFileMetadata();
        String content = fileUpload.getContent();

        Path filePath = Paths.get("files/" + fileMetadata.getFilePath() + "/" + fileMetadata.getFileName());
        if (Files.exists(filePath)) {
            return null;
        }
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, content.getBytes());
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
