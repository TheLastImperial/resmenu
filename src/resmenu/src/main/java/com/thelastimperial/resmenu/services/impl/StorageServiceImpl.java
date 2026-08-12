package com.thelastimperial.resmenu.services.impl;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.thelastimperial.resdomain.entities.StorageEntity;
import com.thelastimperial.resdomain.repositories.StorageRepository;
import com.thelastimperial.resmenu.services.StorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService{
    private final Path rootPath;
    private final List<MediaType> mediaTypeAllow;
    private final StorageRepository storageRepository;

    public StorageServiceImpl(
        @Value("${com.thelastimperial.resmenu.storage.path}")
        String storagePath,
        @Value("${com.thelastimperial.resmenu.storage.media-types}")
        List<String> mediaTypes,
        StorageRepository storageRepository
    ){
        log.info("Using path: {}", storagePath);
        this.rootPath = Paths.get(storagePath);
        this.storageRepository = storageRepository;
        this.mediaTypeAllow = mediaTypes.stream()
            .map(m -> MediaType.valueOf(m))
            .collect(Collectors.toList());
    }

    @Override
    public StorageEntity create(MultipartFile file) throws Exception {
        if(
            !mediaTypeAllow
                .contains(MediaType.valueOf(file.getContentType()))
        ){
            log.error("Content Type: {}", file.getContentType());
            log.error("Just can send: {}", mediaTypeAllow);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        StorageEntity toSave = StorageEntity.builder()
            .filename(file.getOriginalFilename())
            .contentType(file.getContentType())
            .byteSize(
                BigInteger.valueOf(file.getBytes().length)
            )
            .checksum(getChecksum(file.getInputStream()))
        .build();
        StorageEntity saved = storageRepository.save(toSave);
        Path dest = rootPath
            .resolve(
                saved.getId().toString()
            ).normalize().toAbsolutePath();
        Files.copy(
            file.getInputStream(),
            dest,
            StandardCopyOption.REPLACE_EXISTING
        );
        return saved;
    }

    @Override
    public void delete(UUID id) {
        storageRepository.deleteById(id);
    }

    @Override
    public StorageEntity get(UUID id) {
        StorageEntity response = storageRepository.findById(id).orElse(null);
        return response;
    }
    
    @Override
    public Resource entityToResource(StorageEntity entity) {
        Path path = rootPath.resolve(
            entity.getId().toString()
        ).normalize().toAbsolutePath();
        Resource resource = new FileSystemResource(path);
        String checksum = "";

        try{
            checksum = getChecksum(resource.getInputStream());
        }catch(Exception e) {
            log.error("Error traying to get InputStream");
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if(!entity.getChecksum().equals(checksum)){
            log.error("Dismatch checksum for storage");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return resource;
    }

    public String getChecksum(InputStream inputStream) {
        
        String result = null;
        try{
            // Initialize SHA-256 digest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Use try-with-resources to ensure the input stream closes
            try (InputStream is = inputStream) {
                byte[] buffer = new byte[8192]; // 8KB buffer size
                int bytesRead;
                
                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            
            // Convert the byte array to a readable hex string
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            result = hexString.toString();
        } catch(Exception e){
            log.error("There is a error trying to generate CheckSum.");
            log.error(e.getMessage());
        }

        return result;
    }
}
