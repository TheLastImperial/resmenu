package com.thelastimperial.resmenu.services;

import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.thelastimperial.resmenu.entities.StorageEntity;

public interface StorageService {
    public StorageEntity create(MultipartFile file) throws Exception;
    public void delete(UUID id);
    public StorageEntity get(UUID id);
    public Resource entityToResource(StorageEntity entity);
}
