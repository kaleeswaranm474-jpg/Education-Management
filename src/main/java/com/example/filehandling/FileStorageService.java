package com.example.filehandling;

import com.example.exception.BadRequestException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService() {

        this.fileStorageLocation = Paths.get("uploads")
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException exception) {
            throw new RuntimeException(
                    "Could not create upload directory",
                    exception
            );
        }
    }

    public String storeFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File cannot be empty");
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (fileName.isBlank()) {
            throw new RuntimeException("Invalid file name");
        }

        try {

            Path targetLocation = this.fileStorageLocation
                    .resolve(fileName)
                    .normalize();

            if (!targetLocation.startsWith(this.fileStorageLocation)) {
                throw new BadRequestException(
                        "Invalid file path"
                );
            }

            if (Files.exists(targetLocation)) {
                throw new BadRequestException(
                        "Invalid Data: File already exists: " + fileName
                );
            }

            Files.copy(
                    file.getInputStream(),
                    targetLocation
            );

            return fileName;

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Could not store file: " + fileName,
                    exception
            );
        }
    }

    public Resource loadFileAsResource(String fileName) {

        try {

            Path filePath = this.fileStorageLocation
                    .resolve(fileName)
                    .normalize();

            Resource resource = new UrlResource(
                    filePath.toUri()
            );

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException(
                    "File not found: " + fileName
            );

        } catch (MalformedURLException exception) {

            throw new RuntimeException(
                    "File not found: " + fileName,
                    exception
            );
        }
    }
}