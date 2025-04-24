package com.fastfile.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchFileDTO {
    public String fileName;
    public String directory;
}