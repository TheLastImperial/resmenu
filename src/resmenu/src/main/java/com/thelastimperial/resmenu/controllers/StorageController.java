package com.thelastimperial.resmenu.controllers;

import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thelastimperial.resmenu.entities.StorageEntity;
import com.thelastimperial.resmenu.services.StorageService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/r")
@Slf4j
public class StorageController {
    private final StorageService storageService;

    public StorageController(
        StorageService storageService
    ){
        this.storageService = storageService;
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getResource(@PathVariable UUID fileId) {
        StorageEntity storage = storageService.get(fileId);
        log.info("FileId: {}", fileId);
        Resource resource = storageService.entityToResource(storage);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
            ContentDisposition
                .inline()
                .filename(storage.getFilename())
                .build()
        );
        return ResponseEntity.ok()
                .contentType(
                    MediaType.valueOf(storage.getContentType())
                )
                // .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                .headers(headers)
                .body(resource);
    }
}
