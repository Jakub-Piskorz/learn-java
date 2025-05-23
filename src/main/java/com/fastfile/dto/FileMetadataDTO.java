package com.fastfile.dto;

// Simple DTO to hold metadata
public record FileMetadataDTO(String name, long size, long lastModified, String type, String path) {}