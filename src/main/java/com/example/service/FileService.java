package com.example.service;

import com.example.filehandling.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileStorageService fileStorageService;

    public String uploadFile(MultipartFile file) {
        return fileStorageService.storeFile(file);
    }

    public Resource downloadFile(String fileName) {
        return fileStorageService.loadFileAsResource(fileName);
    }
}