package com.example.repository;

import com.example.model.FileMetadata;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Integer> {
    @Nullable
    Optional<FileMetadata> findById(Long id);
}
