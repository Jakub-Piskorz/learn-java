package com.example.dto;

// Simple DTO to hold metadata
public record FileMetadataDTO(String name, long size, long lastModified, String type) {}