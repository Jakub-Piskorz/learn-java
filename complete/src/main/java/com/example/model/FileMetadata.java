package com.example.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @NonNull
    @Column(nullable = false)
    private String createdAt;

    @NonNull
    @Column(nullable = false)
    private Long ownerId;

    // No-arguments constructor for Hibernate
    public FileMetadata() {
    }

}
