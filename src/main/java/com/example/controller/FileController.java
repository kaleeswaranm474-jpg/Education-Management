package com.example.controller;

import com.example.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "Upload a file",
            description = "Upload a file to the server"
    )
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file) {

        String fileName = fileService.uploadFile(file);

        return ResponseEntity.ok(
                "File uploaded successfully: " + fileName
        );
    }

    @Operation(
            summary = "Download a file",
            description = "Download a file from the server"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File downloaded successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                    schema = @Schema(
                            type = "string",
                            format = "binary"
                    )
            )
    )
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileName) {

        Resource resource = fileService.downloadFile(fileName);

        return ResponseEntity.ok()
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() +
                                "\""
                )
                .body(resource);
    }
}