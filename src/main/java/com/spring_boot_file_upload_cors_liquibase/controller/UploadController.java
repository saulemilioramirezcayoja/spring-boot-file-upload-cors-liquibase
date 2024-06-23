package com.spring_boot_file_upload_cors_liquibase.controller;

import com.spring_boot_file_upload_cors_liquibase.model.FileModel;
import com.spring_boot_file_upload_cors_liquibase.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final UploadService uploadService;

    @Autowired
    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestBody Map<String, String> data) {
        String fileName = data.get("file");
        String base64 = data.get("base64");
        if (fileName == null) {
            return ResponseEntity.badRequest().body("File name is required.");
        }
        if (base64 == null) {
            return ResponseEntity.badRequest().body("Base64 data is required.");
        }

        try {
            String filePath = uploadService.storeFile(fileName, base64);
            return ResponseEntity.ok("File received: " + fileName + " stored at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to store file: " + e.getMessage());
        }
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<ByteArrayResource> getFileById(@PathVariable Long id) {
        try {
            FileModel fileModel = uploadService.getFileModelById(id);
            byte[] data = uploadService.getFileById(id);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileModel.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileModel.getName() + "\"")
                    .body(new ByteArrayResource(data));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}