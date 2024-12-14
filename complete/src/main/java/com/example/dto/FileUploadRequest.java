package com.example.dto;

import com.example.model.FileMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileUploadRequest {

    private FileMetadata fileMetadata;
    private String content;

    public FileUploadRequest(FileMetadata fileMetadata, String content) {
        this.fileMetadata = fileMetadata;
        this.content = content;
    }


}
