package com.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileDTO {

    private String fileFolder;
    private MultipartFile file;

    public FileDTO(String filePath, MultipartFile file) {
        this.fileFolder = filePath;
        this.file = file;
    }
}
