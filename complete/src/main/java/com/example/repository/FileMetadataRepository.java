package com.example.repository;

import com.example.model.FileMetadata;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Integer> {
    @Nullable
    FileMetadata findById(Long id);
}
